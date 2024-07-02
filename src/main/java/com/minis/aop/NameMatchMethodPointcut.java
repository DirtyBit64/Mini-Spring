package com.minis.aop;

import com.minis.util.PatterMatchUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class NameMatchMethodPointcut implements MethodMatcher, Pointcut{
    private String mappedName = "";

    @Override
    public boolean matches(Method method, Class<?> targetCLass) {
        if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
            return true;
        }
        return false;
    }
    //核心方法，判断方法名是否匹配给定的模式
    protected boolean isMatch(String methodName, String mappedName) {
        return PatterMatchUtils.simpleMatch(mappedName, methodName);
    }
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}