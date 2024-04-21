package com.minis.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 定义一个bean的基本信息
 */
@Data
@AllArgsConstructor
public class BeanDefinition {
    private String id;
    private String className;

}
