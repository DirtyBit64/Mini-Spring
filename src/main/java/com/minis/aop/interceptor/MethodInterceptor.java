package com.minis.aop.interceptor;

import com.minis.aop.MethodInvocation;

public interface MethodInterceptor extends Interceptor{
    Object invoke(MethodInvocation invocation) throws Throwable;
}