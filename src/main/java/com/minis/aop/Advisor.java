package com.minis.aop;

import com.minis.aop.interceptor.MethodInterceptor;

// 拦截器的封装
public interface Advisor {
    MethodInterceptor getMethodInterceptor();
    void setMethodInterceptor(MethodInterceptor methodInterceptor);
}