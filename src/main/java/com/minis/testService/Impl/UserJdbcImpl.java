package com.minis.testService.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.testController.pojo.Student;

public class UserJdbcImpl extends JdbcTemplate {
    protected Object doInStatement(ResultSet rs) {
        //从jdbc数据集读取数据，并生成对象返回
        Student rtnUser = null;
        try {
            if (rs.next()) {
                rtnUser = new Student();
                rtnUser.setId(rs.getLong("id"));
                rtnUser.setName(rs.getString("name"));
                rtnUser.setGrade(rs.getInt("grade"));
                rtnUser.setAge(rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rtnUser;
    }
}