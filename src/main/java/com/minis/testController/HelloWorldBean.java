package com.minis.testController;

import com.minis.web.RequestMapping;

public class HelloWorldBean {

    @RequestMapping(value = "/get")
    public String doGet() {
        return "hello world get!";
    }
    @RequestMapping(value = "/post")
    public String doPost() {
        return "hello world post!";
    }
}