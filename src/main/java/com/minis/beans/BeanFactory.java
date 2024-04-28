package com.minis.beans;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBean(String beanName, Object obj);
    Boolean containsBean(String name);
}
