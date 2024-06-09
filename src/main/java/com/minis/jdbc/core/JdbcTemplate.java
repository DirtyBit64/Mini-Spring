package com.minis.jdbc.core;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.core.StatementCallback;
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
}
