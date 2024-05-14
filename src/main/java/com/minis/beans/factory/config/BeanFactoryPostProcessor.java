package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    // 用于给beanFactory和PostProcessor建立桥梁，避免难懂的循环引用
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
