package com.minis.testController;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.testService.AService;
import com.minis.web.RequestMapping;

public class HelloWorldBean {

    @Autowired
    private AService aService;

    @RequestMapping(value = "/get")
    public String doGet() {
        return "hello world get!";
    }
    @RequestMapping(value = "/aService")
    public String doPost() {
        return aService.toString();
    }
}