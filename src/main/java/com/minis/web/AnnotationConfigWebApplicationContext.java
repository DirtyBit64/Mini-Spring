package com.minis.web;

import javax.servlet.ServletContext;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext{
    private ServletContext servletContext;

    public AnnotationConfigWebApplicationContext(String fileName) throws BeansException {
        super(fileName, true);
    }
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}