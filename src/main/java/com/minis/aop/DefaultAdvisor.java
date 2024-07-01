package com.minis.aop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DefaultAdvisor implements Advisor{
    // 方法拦截器
    private MethodInterceptor methodInterceptor;
}
