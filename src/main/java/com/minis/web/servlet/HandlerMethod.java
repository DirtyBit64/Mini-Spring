package com.minis.web.servlet;

import com.sun.org.apache.bcel.internal.classfile.MethodParameter;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class HandlerMethod {
    private  Object bean;
    private  Class<?> beanType;
    private  Method method;
    private  MethodParameter[] parameters;
    private  Class<?> returnType;
    private  String description;
    private  String className;
    private  String methodName;

    public HandlerMethod(Method method, Object obj) {
        this.setMethod(method);
        this.setBean(obj);
    }
}