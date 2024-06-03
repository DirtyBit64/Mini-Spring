package com.minis.testController;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.testController.pojo.User;
import com.minis.testService.AService;
import com.minis.web.RequestMapping;

public class HelloWorldBean {

    @Autowired
    private AService aService;

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
}