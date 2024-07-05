package com.minis.aop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameMatchMethodPointcutAdvisor implements PointcutAdvisor{
    private Advice advice;
    private MethodInterceptor methodInterceptor;
    // 配置文件注入进来的匹配规则
    private String mappedName;
    private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    public NameMatchMethodPointcutAdvisor() {
    }

    // 完成一些初始化操作
    private void init(){
        setMappedName(mappedName);
        setAdvice(advice);
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
        MethodInterceptor mi = null;
        if (advice instanceof BeforeAdvice) {
            mi = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice)advice);
        } else if (advice instanceof AfterAdvice){
            mi = new AfterReturningAdviceInterceptor((AfterReturningAdvice)advice);
        } else if (advice instanceof MethodInterceptor) {
            mi = (MethodInterceptor)advice;
        }
        setMethodInterceptor(mi);
    }

    // 设置规则
    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
        this.pointcut.setMappedName(this.mappedName);
    }

    @Override
    public void setAdvice(Advisor advice) {

    }
}