package com.minis.aop.interceptor;

import com.minis.aop.MethodInvocation;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.transaction.TransactionManager;

// 数据库事务拦截器
public class TransactionInterceptor implements MethodInterceptor{
    @Autowired
    private TransactionManager transactionManager;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        transactionManager.doBegin();
        Object ret=invocation.proceed();
        transactionManager.doCommit();
        return ret;
    }
}