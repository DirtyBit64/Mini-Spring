package com.minis.transaction;

import com.minis.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    @Autowired
    private DataSource dataSource;
    Connection conn = null;

    public void doBegin() throws SQLException {
        conn = dataSource.getConnection();
        if (conn.getAutoCommit()) {
            conn.setAutoCommit(false);
        }
    }
    public void doCommit() throws SQLException {
        conn.commit();
    }
}