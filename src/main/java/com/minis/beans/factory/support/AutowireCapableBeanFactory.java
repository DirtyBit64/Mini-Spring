package com.minis.beans.factory.support;

import com.minis.beans.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.BeansException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AutowireCapableBeanFactory extends AbstractBeanFactory{
    // TODO 为什么是空的
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();
    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : this.beanPostProcessors) {
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
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                break;
            }
        }
        return result;
    }

}