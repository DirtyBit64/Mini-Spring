package com.minis.web.servlet;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MappingRegistry {
    private List<String> urlMappingNames = new ArrayList<>();// 是保存自定义的@RequestMapping名称（URL的名称）的列表
    private Map<String,Object> mappingObjs = new HashMap<>();// 保存URL与对象的映射关系
    private Map<String,Method> mappingMethods = new HashMap<>();// 保存URL名称与方法的映射关系


}