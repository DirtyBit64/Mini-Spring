package com.minis.aop;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.util.ClassUtils;
import com.mysql.cj.protocol.x.XProtocolRowInputStream;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProxyFactoryBean implements FactoryBean<Object> {
    private AopProxyFactory aopProxyFactory;
    private String[] interceptorNames;
    private String targetName;
    private Object target;
    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    private Object singletonInstance;

    // 代理bean构建好后专门setter调用下
    private BeanFactory beanFactory;
    private String interceptorName;

    @Autowired
    private PointcutAdvisor advisor;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    //ioc构建好bean后调用
    private void init(){
        initializeAdvisor();
    }

    // 初始化拦截器
    private synchronized void initializeAdvisor() {
        Object advice = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }

        this.advisor.setAdvice((Advice) advice);
        // this.advisor = (PointcutAdvisor) advice;
    }

    protected AopProxy createAopProxy() {
        return aopProxyFactory.createAopProxy(target, this.advisor);
    }

    //获取内部对象
    @Override
    public Object getObject(){
        return getSingletonInstance();
    }
    //获取代理
    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }
    //生成代理对象
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }
    @Override
    public Class<?> getObjectType() {
        return getObject().getClass();
    }
}