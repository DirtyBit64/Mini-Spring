package com.minis.context;

import com.minis.beans.*;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory {
    private BeanFactory beanFactory;

    /**
     * ctx构造器 -> 整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
     * @param fileName 文件名
     */
    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        BeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((SimpleBeanFactory) beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }

    /**
     * 提供给外部程序从容器中获取Bean实例的接口,调用的BeanFactory对应的方法
     * @param beanID 配置文件中的
     * @return Bean实例
     */
    public Object getBean(String beanID) throws BeansException {
        return this.beanFactory.getBean(beanID);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

}

