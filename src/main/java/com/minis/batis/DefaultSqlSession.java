package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;
import lombok.Getter;

@Getter
public class DefaultSqlSession implements SqlSession{

    JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    SqlSessionFactory sqlSessionFactory;
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        System.out.println(sqlid);
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        System.out.println(sql);

        return jdbcTemplate.query(sql, args, pstmtcallback);
    }

    private void buildParameter(){
    }

    private Object resultSet2Obj() {
        return null;
    }

}