package com.minis.beans.factory.config;

import com.minis.beans.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.BeansException;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    protected final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }
    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : this.beanPostProcessors) {
            beanProcessor.setBeanFactory(this);
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : beanPostProcessors) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                break;
            }
        }
        return result;
    }
    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }

}
