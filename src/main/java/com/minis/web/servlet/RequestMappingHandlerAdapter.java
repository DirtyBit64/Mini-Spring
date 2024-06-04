package com.minis.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.web.WebApplicationContext;
import com.minis.web.WebBindingInitializer;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext wac;
    private WebBindingInitializer webBindingInitializer;


    public RequestMappingHandlerAdapter(WebApplicationContext wac) throws BeansException {
        this.wac = wac;
        // web数据绑定初始化器,初始化用户自定义的一些类型转换器
        this.webBindingInitializer = (WebBindingInitializer)
                this.wac.getBean("webBindingInitializer");
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }
    private void handleInternal(HttpServletRequest request, HttpServletResponse response,
                                HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 反射调用方法并且绑定数据
    protected void invokeHandlerMethod(HttpServletRequest request,
                                       HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();
            Object methodParamObj = type.newInstance(); // 基本数据类型异常
            // 给这个参数创建WebDataBinder   methodParamObj: user
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());

            //注册binder中的editor-- 自定义editer
            webBindingInitializer.initBinder(wdb);

            wdb.bind(request);

            methodParamObjs[i++] = methodParamObj;
        }

        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        response.getWriter().append(returnObj.toString());
    }

}