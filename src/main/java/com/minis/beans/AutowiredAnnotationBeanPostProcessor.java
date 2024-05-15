package com.minis.beans;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        // 对每个属性进行判断，如果带有@Autowired注解则处理
        for (Field field : fields){
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if(isAutowired){
                // 根据属性名查找同名的bean
                String fieldName = field.getName();
                Object autowiredObj = this.beanFactory.getBean(fieldName);
                // 设置属性值，完成注入
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                    System.out.println("autowire " + fieldName + " for bean " + beanName);
                }catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
