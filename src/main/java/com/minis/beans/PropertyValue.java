package com.minis.beans;

import lombok.Data;

@Data
public class PropertyValue {
    private final String type; // 属性类型
    private final String name; // 属性名称
    private final Object value; // 值（当property为引用类型时为空）
    private final boolean isRef; // 是否为引用类型（注入别的bean）

    public boolean getIsRef(){
        // 小bug:为什么lombok没生成这个参数的get方法呢
        return this.isRef;
    }

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

}