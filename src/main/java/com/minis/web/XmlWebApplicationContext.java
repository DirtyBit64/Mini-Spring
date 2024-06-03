package com.minis.web;

import javax.servlet.ServletContext;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext{
    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) throws BeansException {
        super(fileName, true);
    }

}