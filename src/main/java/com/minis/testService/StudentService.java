package com.minis.testService;

import com.minis.batis.SqlSession;
import com.minis.batis.SqlSessionFactory;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.testController.pojo.Student;

import java.sql.ResultSet;

public class StudentService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Student getUserInfo(long userid) {
        // 相当于service调mapper了
        String sqlid = "com.minis.testController.pojo.Student.getUserInfo";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (Student) sqlSession.selectOne(sqlid, new Object[]{userid},
                (stmt)->{
                    ResultSet rs = stmt.executeQuery();
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