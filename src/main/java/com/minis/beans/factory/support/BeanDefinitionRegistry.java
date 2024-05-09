package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * 存放BeanDefinition的仓库，可以存放、移除、获取及判断BeanDefinition对象
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition bd);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}