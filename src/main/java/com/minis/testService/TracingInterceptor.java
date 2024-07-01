package com.minis.testService;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

public class TracingInterceptor implements MethodInterceptor {

    // 实现业务增强方法
    @Override
    public Object invoke(MethodInvocation i) throws Throwable {
        System.out.println("method "+i.getMethod()+" is called on "+
                i.getThis()+" with args "+i.getArguments());
        Object ret = i.proceed();
        System.out.println("method "+i.getMethod()+" returns "+ret);
        return ret;
    }
}