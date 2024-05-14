package com.minis.context;

import com.minis.beans.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.support.AutowireCapableBeanFactory;
import com.minis.beans.factory.support.SimpleBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    private AutowireCapableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    /**
     * ctx构造器 -> 整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
     * @param fileName 文件名
     */
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) throws BeansException {
        Resource resource = new ClassPathXmlResource(fileName);
        this.beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        registerBeanPostProcessors(this.beanFactory); // 先这样，之后再看
        reader.loadBeanDefinitions(resource); // 懒加载逻辑在这,创建bean
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

    public void refresh() throws IllegalStateException {
        // 注册紧跟beanFactory创建,提上去  1.注册拦截bean创建的bean处理器
        // registerBeanPostProcessors(this.beanFactory);
        // 2.初始化特定上下文子类中的其他特殊bean
        onRefresh();
    }

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

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh(){
        this.beanFactory.refresh();
    }
}

