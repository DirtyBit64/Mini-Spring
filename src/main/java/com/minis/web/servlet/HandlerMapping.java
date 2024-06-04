package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    //建立URL与调用方法和实例的映射关系，存储在mappingRegistry中
    void initMapping();

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;//根据访问URL查找对应的调用方法
    void setWac(WebApplicationContext wac);
}
