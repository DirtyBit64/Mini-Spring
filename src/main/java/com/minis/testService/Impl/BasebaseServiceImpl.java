package com.minis.testService.Impl;

import com.minis.testService.AService;
import com.minis.testService.BasebaseService;
import lombok.Setter;

@Setter
public class BasebaseServiceImpl implements BasebaseService {

    // 循环注入 糟糕的代码才会如此，没有层级观念的项目，spring6开始限制
    private AService as;

    @Override
    public void test() {
        System.out.println("I am basebaseService");
    }
}
