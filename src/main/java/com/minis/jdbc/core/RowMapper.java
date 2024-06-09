package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

// 把JDBC返回的ResultSet里的某一行数据映射成一个对象
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}