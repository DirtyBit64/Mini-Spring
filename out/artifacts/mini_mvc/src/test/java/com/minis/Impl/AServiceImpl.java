package com.minis.Impl;

import com.minis.AService;
import com.minis.BaseService;
import com.minis.BasebaseService;
import com.minis.beans.factory.annotation.Autowired;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class AServiceImpl implements AService {
    private String property1;
    private String property2;

    @Autowired
    private BaseService baseService;

    private String name;
    private int level;
    // Bean注入 为什么没生成setter
    private BaseService ref1;

    public AServiceImpl(String name, int level){
        this.name = name;
        this.level = level;
    }

    @Override
    public void sayHello() {
        System.out.println(property1 + property2);
    }

    @Override
    public void sayMyName(){
        System.out.println(name + ":" + level);
    }

    @Override
    public void test(){
        this.baseService.test();
    }

}
