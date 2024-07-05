package com.minis.aop.framework.autoproxy;

import com.minis.aop.AopProxyFactory;
import com.minis.aop.PointcutAdvisor;
import com.minis.aop.ProxyFactoryBean;
import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.util.PatterMatchUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 根据bean的名字匹配自动创建动态代理
@NoArgsConstructor
@Setter
@Getter
public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    //代理对象名称模式，如action*
    String pattern;
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String interceptorName;
    private PointcutAdvisor advisor;

    //核心方法。在bean实例化之后，init-method调用之前执行这个步骤。
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isMatch(beanName, this.pattern)) {
            //创建一个ProxyFactoryBean
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            this.aopProxyFactory = proxyFactoryBean.getAopProxyFactory();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            return proxyFactoryBean;
        }
        else {
            return bean;
        }
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatterMatchUtils.simpleMatch(mappedName, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}