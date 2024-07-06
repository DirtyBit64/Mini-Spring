package com.minis.aop.interceptor;

import com.minis.aop.BeforeAdvice;
import com.minis.aop.MethodBeforeAdvice;
import com.minis.aop.MethodInvocation;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice {
    private final MethodBeforeAdvice advice;
    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}