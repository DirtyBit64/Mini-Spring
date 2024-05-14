package com.minis;

import com.minis.Impl.AServiceImpl;
import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.context.ClassPathXmlApplicationContext;

public class testMain {

    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml", true);
        AService aService = (AService) ctx.getBean("aService");
        aService.sayHello();
        aService.sayMyName();
        aService.test();
    }
}
