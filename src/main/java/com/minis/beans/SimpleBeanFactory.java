package com.minis.beans;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory{
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(256);

    /**
     * 提供给外部程序从容器中获取Bean实例的接口
     * @param beanID 配置文件中的
     * @return Bean实例
     */
    public Object getBean(String beanID) throws BeansException {
        //先尝试直接拿Bean实例对象
        Object singleton = this.singletons.get(beanID);
        //如果此时没有该Bean实例，则获取它的定义来创建实例
        if(singleton == null){
            BeanDefinition beanDefinition = beanDefinitions.get(beanID);
            if(beanDefinition == null){
                throw new BeansException("未在配置文件中读取到该Bean");
            }
            try {
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                log.error("创建Bean实例异常:", e);
            }
            // 新注册该Bean实例
            log.info("注册Bean:{}", beanDefinition);
            this.registerSingleton(beanID, singleton);
        }
        return singleton;
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.containsBean(name);
    }

    /**
     * 1.注册Bean的定义，形成内存映像
     * 2.保存beanID(beanName)
     * @param beanDefinition
     */
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }
}
