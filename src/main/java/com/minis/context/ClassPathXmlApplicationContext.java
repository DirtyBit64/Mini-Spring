package com.minis.context;

import com.minis.beans.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.support.SimpleBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    private SimpleBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName){
        this(fileName, true);
    }

    /**
     * ctx构造器 -> 整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
     * @param fileName 文件名
     */
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        this.beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        if(isRefresh){
            // 激活容器，加载所有bean
            this.beanFactory.refresh();
        }
    }

    /**
     * 提供给外部程序从容器中获取Bean实例的接口,调用的BeanFactory对应的方法
     * @param beanID 配置文件中的
     * @return Bean实例
     */
    public Object getBean(String beanID) throws BeansException {
        return this.beanFactory.getBean(beanID);
    }

//    @Override
//    public void registerBean(String beanName, Object obj) {
//        this.beanFactory.registerBean(beanName, obj);
//    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent applicationEvent) {

    }
}

