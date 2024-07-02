package com.minis.aop;

public class DefaultAopProxyFactory implements AopProxyFactory{
    // 代理类工厂，经典工厂模式
    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}