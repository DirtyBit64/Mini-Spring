package com.minis.beans;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    Boolean containsBean(String name);
    //void registerBean(String beanName, Object obj);
    boolean isSingleton(String name); // 是否单例
    boolean isPrototype(String name); // 是否原型
    Class<?> getType(String name); // 获取bean类型
}
