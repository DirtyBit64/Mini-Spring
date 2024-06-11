package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;
import com.minis.jdbc.core.StatementCallback;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
    Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}