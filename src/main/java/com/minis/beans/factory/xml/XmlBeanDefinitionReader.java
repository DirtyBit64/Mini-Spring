package com.minis.beans.factory.xml;

import com.minis.beans.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;
    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 解析XML配置信息并加载到BeanDefinition
     * @param resource 资源实例
     */
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            // 处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                Object aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(aValue, aType, aName));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            // 解析属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for(Element e : propertyElements){
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.isEmpty()){
                    pV = pValue;
                }else if(pRef != null && !pRef.isEmpty()){
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            this.beanFactory.registerBeanDefinition(beanID, beanDefinition);
        }
        // 等所有beandefinition注册加载完毕后再加载bean
        for (String bdName : this.beanFactory.getBeanDefinitionNames()){
            BeanDefinition bd = this.beanFactory.getBeanDefinitionMap().get(bdName);
            // 如果没有选择懒加载
            if (!bd.isLazyInit()) {
                try {
                    this.beanFactory.getBean(bd.getId());
                } catch (BeansException e) {
                    log.error("registerBeanDefinition()发生异常", e);
                }
            }
        }

    }
}
