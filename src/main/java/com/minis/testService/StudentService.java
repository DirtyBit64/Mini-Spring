package com.minis.testService;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.testController.pojo.Student;

import java.sql.ResultSet;

public class StudentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Student getUserInfo(long userid) {
        // 相当于service调mapper了
        final String sql = "select id, name, grade from student where id="+userid;
        return (Student) jdbcTemplate.query(
                (stmt)->{
                    ResultSet rs = stmt.executeQuery(sql);
                    Student rtnUser = null;
                    if (rs.next()) {
                        rtnUser = new Student();
                        rtnUser.setId(userid);
                        rtnUser.setName(rs.getString("name"));
                        rtnUser.setGrade(rs.getInt("grade"));
                    }
                    return rtnUser;
                }
        );
    }

}