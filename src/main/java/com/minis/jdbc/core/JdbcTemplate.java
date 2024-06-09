package com.minis.jdbc.core;

import com.minis.beans.factory.annotation.Autowired;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class JdbcTemplate {

    @Autowired
    private DataSource dataSource;

    public Object query(StatementCallback statementCallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            return statementCallback.doInStatement(stmt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                stmt.close();
                con.close();
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
}
