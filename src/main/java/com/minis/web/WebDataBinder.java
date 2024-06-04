package com.minis.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyValues;
import com.minis.util.WebUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebDataBinder {
    private Object target;  // 目标对象
    private Class<?> clz; // 目标对象字节码
    private String objectName; // 目标对象名称
    public WebDataBinder(Object target) {
        this(target, "");
    }
    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
    }

    //核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) {
        // 1.把request里的参数解析成PropertyValue
        PropertyValues mpvs = assignParameters(request);
        // 2.把request里的参数值添加到绑定参数中
        addBindValues(mpvs, request);
        // 3.把两者绑定到一起
        doBind(mpvs);
    }
    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }
    //实际将属性map绑定到目标对象上
    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }
    //设置属性值的工具
    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }
    //将Request参数解析成PropertyValues
    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }
    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
    }
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
        log.info("注册自定义{}类型的参数转换器", requiredType);
    }
}