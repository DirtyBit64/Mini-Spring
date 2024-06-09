package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

// 把JDBC返回的ResultSet数据集映射为一个集合对象
public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
}