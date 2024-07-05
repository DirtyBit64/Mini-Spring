package com.minis.context;

import com.minis.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import com.minis.beans.*;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

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
        // 懒加载逻辑在这,创建bean
        reader.loadBeanDefinitions(resource);
        // 先加载bean定义再注册后处理器,因为beanPostProcessor统一交给ioc管理了
        registerBeanPostProcessors(this.beanFactory);
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

    @Override
    public void registerBean(String beanName, Object obj) {

    }

    /**
     * 注册事件监听器
     */
    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 注册Autowired后处理器
        this.beanFactory.addBeanPostProcessor((AutowiredAnnotationBeanPostProcessor) beanFactory.getBean("autowiredAnnotationBeanPostProcessor"));
        // 注册自动代理后处理器
        this.beanFactory.addBeanPostProcessor((BeanNameAutoProxyCreator) beanFactory.getBean("autoProxyCreator"));
    }

    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    protected void finishRefresh() {
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

    @Override
    public String[] getBeanDefinitionNamesAsArray() {
        return this.beanFactory.getBeanDefinitionNamesAsArray();
    }
}

