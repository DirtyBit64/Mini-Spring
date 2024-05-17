package com.minis.context;

import com.minis.beans.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.core.env.Environment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory beanFactory;

    /**
     * ctx构造器 -> 整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
     * @param fileName 文件名
     */
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) throws BeansException {
        Resource resource = new ClassPathXmlResource(fileName);
        this.beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        registerBeanPostProcessors(this.beanFactory); // 1.先这样，之后再看
        reader.loadBeanDefinitions(resource); // 2.懒加载逻辑在这,创建bean
        if(isRefresh){
            // 激活容器，加载所有bean
            refresh();
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

//    public void refresh() throws IllegalStateException {
//        // 注册紧跟beanFactory创建,提上去  1.注册拦截bean创建的bean处理器
//        // registerBeanPostProcessors(this.beanFactory);
//        // 2.初始化特定上下文子类中的其他特殊bean
//        onRefresh();
//    }

    /**
     * 注册事件监听器
     */
    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    void initApplicationEventPublisher() {
        // 先采用简单实现
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void publishEvent(ApplicationEvent applicationEvent) {
        this.getApplicationEventPublisher().publishEvent(applicationEvent);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }


    private void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }


    @Override
    public String[] getBeanDefinitionNamesAsArray() {
        return this.beanFactory.getBeanDefinitionNamesAsArray();
    }
}

