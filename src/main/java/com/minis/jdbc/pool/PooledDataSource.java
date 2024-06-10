package com.minis.jdbc.pool;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class PooledDataSource implements DataSource {
    // 生产者消费者模型 线程安全
    private BlockingQueue<PooledConnection> busy;// 使用中的连接  消费 只写入
    private BlockingQueue<PooledConnection> idle;// 空闲的连接 生产 只读取

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 2;
    private Properties connectionProperties;
    // 等待时间
    private int loginTimeout;
    // 重试最大次数
    private int maxRetry;
    // 最大维护连接数
    private int maxActive;

    // 构造器注入
    public PooledDataSource(String driverClassName, String url, String username, String password, int initialSize){
        this.driverClassName = driverClassName;
        // 加载驱动
        try {
            Class.forName(this.driverClassName);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
        }
        this.url = url;
        this.username = username;
        this.password = password;
        this.initialSize = initialSize;
        this.maxActive = initialSize;
        try {
            initPool();
        } catch (SQLException e) {
            log.error("error in initPool: 数据源连接池初始化失败", e);
        }
    }

    // 连接池初始化
    private void initPool() throws SQLException {
        this.busy = new ArrayBlockingQueue<>(initialSize);//无需考虑读写锁分离
        this.idle = new ArrayBlockingQueue<>(initialSize);

        this.setLoginTimeout(1);
        this.setMaxRetry(3); // 最多重试3次

        Connection connect = DriverManager.getConnection(url, username, password);
        for(int i = 0; i < initialSize; i++){
            PooledConnection pooledConnection = new PooledConnection(connect, false);
            this.idle.add(pooledConnection);
        }
        log.info("initPool(): 数据库连接池完成初始化!");
    }

    @Override
    public Connection getConnection() throws SQLException {
        PooledConnection pooledConnection= getAvailableConnection();
        int count = 0;
        // 死循环等待有效连接
        while(pooledConnection == null && count < this.getMaxRetry()){
            pooledConnection = getAvailableConnection();
            ++count;
        }

        if(pooledConnection != null){
            this.busy.offer(pooledConnection);
            return  pooledConnection;
        }

        // 没获取到，看看是否能新建一个
        try {
            // 判断当前连接是否达到最大值，如果没达到，新建连接
            // CAS机制校验
            if (busy.size() < getMaxActive()) {
                if (busy.size() + 1 <= getMaxActive()) {
                    // +1后没超过，创建
                    PooledConnection newCon = new PooledConnection(
                            DriverManager.getConnection(url, username, password), false);
                    setMaxActive(getMaxActive() + 1);
                    busy.add(newCon);
                    pooledConnection = newCon;
                }
            }else{
                throw new SQLException("获取连接失败，服务器忙!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return pooledConnection;
    }

    // 释放连接
    public void releaseConnection(Connection con){

        if(con == null){
            return;
        }

        boolean isRemove = false;
        if(busy.contains(con)){
            isRemove = busy.remove(con);
        }

        // 如果idle没满
        if(isRemove && idle.size() < this.getMaxRetry()){
            idle.add((PooledConnection) con);
        }
    }


    // 获取一个可用连接
    private PooledConnection getAvailableConnection() throws SQLException{
        try {
            return this.idle.poll(this.getLoginTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}