package com.minis.beans.factory.config;

import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import lombok.Data;

/**
 * 定义一个bean的基本信息
 */
@Data
public class BeanDefinition {
    private String id;
    private String className;
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    private boolean lazyInit = false; // bean是否懒加载
    private String[] dependsOn; // 记录Bean之间的依赖关系
    private ConstructorArgumentValues constructorArgumentValues; // constructor注入参数
    private PropertyValues propertyValues; // setter注入参数
    private String initMethodName; // 当一个Bean构造好并实例化之后是否要让框架调用初始化方法
    private volatile Object beanClass;
    private String scope = SCOPE_SINGLETON; // 单例模式/原型模式
    public BeanDefinition(String id, String className) {
        this.id = id; this.className = className;
    }

    public boolean isPrototype() {
        return scope.equals(SCOPE_PROTOTYPE);
    }
    public boolean isSingleton() {
        return scope.equals(SCOPE_SINGLETON);
    }
}
