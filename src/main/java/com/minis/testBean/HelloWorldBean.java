package com.minis.testBean;

import com.minis.web.RequestMapping;

public class HelloWorldBean {
    public String doGet() {
        return "hello world!";
    }
    @RequestMapping(value = "/post")
    public String doPost() {
        return "hello world post!";
    }
}