package com.minis.testController;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.testController.pojo.Student;
import com.minis.testController.pojo.User;
import com.minis.testService.AService;
import com.minis.testService.StudentService;
import com.minis.web.annotation.RequestMapping;
import com.minis.web.annotation.ResponseBody;

import java.util.Date;

public class HelloWorldBean {

    @Autowired
    private AService aService;
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/get")
    public String doGet(User user) {
        return user.getId() +" "+user.getName() + " ";
    }

    @RequestMapping(value = "/get1")
    public String haha(Integer simple) {
        return simple + " ";
    }

    @RequestMapping(value = "/aService")
    public String doPost() {
        return aService.toString();
    }

    @RequestMapping("/test6")
    @ResponseBody
    public User doTest6(User user) {
        return user;
    }
    @RequestMapping("/test7")
    @ResponseBody
    public Student doTest7() {
        return studentService.getUserInfo(8L);
    }
}