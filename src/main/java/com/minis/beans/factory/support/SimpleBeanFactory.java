package com.minis.beans.factory.support;

import com.minis.beans.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
@Data
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 提供给外部程序从容器中获取Bean实例的接口
     * @param beanID 配置文件中的
     * @return Bean实例
     */
    public Object getBean(String beanID) throws BeansException {
        //先尝试直接从容器中拿Bean实例对象
        Object singleton = this.singletons.get(beanID);
        //如果此时没有该Bean实例，则获取它的bd来创建实例
        if(singleton == null){
            // 如果没有实例，则尝试从毛坯实例中获取 ---> 循环依赖的解决方案
            singleton = this.earlySingletonObjects.get(beanID);
            if (singleton == null){
                // 如果毛坯都没有，则创建bean实例并注册
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanID);
                if(beanDefinition == null){
                    throw new BeansException("未在配置文件中读取到" + beanID);
                }
                singleton = createBean(beanDefinition);
                // 注册该Bean实例
                log.info("注册Bean:{}", beanDefinition);
                this.registerSingleton(beanID, singleton);
                // TODO 预留beanpostprocessor位置
                // step 1: postProcessBeforeInitialization
                // step 2: afterPropertiesSet
                // step 3: init-method
                // step 4: postProcessAfterInitialization
            }
        }
        return singleton;
    }

    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public Boolean containsBean(String name) {
        return super.containsSingleton(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getBeanClass().getClass();
        // return this.beanDefinitionMap.get(name).getClass();
    }

    /**
     * 1.注册Bean的定义，形成内存映像
     * 2.保存beanID(beanName)
     * @param beanDefinition
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
//        if (!beanDefinition.isLazyInit()) { 这里先不加载避免抛出不必要的异常，等所有beandefinition加载后再去加载bean
//            try {
//                getBean(name);
//            } catch (BeansException e) {
//                log.error("registerBeanDefinition发生异常", e);
//            }
//        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        // 创建毛坯bean实例
        Object obj = doCreateBean(beanDefinition);
        // 存放(注册)到毛坯实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        }catch (ClassNotFoundException e){
            log.error("找不到{}类", beanDefinition.getClassName(), e);
        }
        // 处理属性
        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    //负责创建毛坯实例，仅仅调用构造方法，未进行属性处理
    private Object doCreateBean(BeanDefinition bd){
        Class<?> clz;
        Object obj = null;
        Constructor<?> con;
        try {
            clz = Class.forName(bd.getClassName());
            // 处理构造器参数
            ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
            //如果有参数
            if (!argumentValues.isEmpty()) {
                // 字节码参数类型数组
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                //对每一个参数，分数据类型分别处理
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) ||
                            "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String)argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else { //默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    //按照特定构造器创建实例
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                }catch (Exception e){
                    log.error("exception from doCreateBean", e);
                }
            } else {
                //如果没有构造参数，先创建实例之后在setter注入
                obj = clz.newInstance();
            }
        } catch (Exception e) {
            log.error("exception from doCreateBean", e);
        }
        log.info(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());
        return obj;
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj){
        // 处理属性
        PropertyValues propertyValues = bd.getPropertyValues();
        // 如果有属性
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef){
                    // 如果不是ref，只是普通属性
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) ||
                            "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else { // 默认为string
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = pValue;
                }else { // 是ref，先创建依赖的beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        log.error("加载不到{}类型", pType, e);
                    }
                    try { //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String)pValue);
                    }catch (BeansException e){
                        log.error("exception from getBean", e);
                    }
                }
                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase()
                        + pName.substring(1);
                Method method;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                try {
                    // 遍历property并调用其setter方法
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                log.error("exception from refresh()...", e);
            }
        }
    }
}
