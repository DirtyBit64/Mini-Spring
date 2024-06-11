package com.minis.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback {
    Object doInStatement(String sql, Statement statement) throws SQLException;
}
