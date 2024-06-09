package com.minis.core;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback {
    Object doInStatement(Statement statement) throws SQLException;
}
