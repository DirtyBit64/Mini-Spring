package com.minis.jdbc.core;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.pool.PooledDataSource;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@NoArgsConstructor
@Setter
public class JdbcTemplate {

    @Autowired
    private DataSource dataSource;

    public Object query(StatementCallback statementCallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();

            if(con == null){
                return "请求失败，服务器忙!";
            }

            stmt = con.createStatement();

            return statementCallback.doInStatement(stmt);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stmt.close();
                con.close();
                if (dataSource instanceof PooledDataSource){
                    PooledDataSource dataSource1 = (PooledDataSource) dataSource;
                    dataSource1.releaseConnection(con);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    // preparedStatement执行sql
    public Object query(String sql, Object[] args, PreparedStatementCallback pstmtcallback) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement(sql);
            ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
            argumentPreparedStatementSetter.setValues(pstmt);

            return pstmtcallback.doInPreparedStatement(pstmt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {

            }
        }

        return null;
    }

    // 提供集合查询
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs;

        try {
            //建立数据库连接
            con = dataSource.getConnection();

            //准备SQL命令语句
            pstmt = con.prepareStatement(sql);
            //设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            //执行语句
            rs = pstmt.executeQuery();

            //数据库结果集映射为对象列表，返回
            return resultExtractor.extractData(rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
