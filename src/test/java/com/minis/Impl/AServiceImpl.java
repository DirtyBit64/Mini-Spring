package com.minis.Impl;

import com.minis.AService;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class AServiceImpl implements AService {
    private String property1;
    private String property2;

    private String name;
    private int level;

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
        System.out.println(name + level);
    }

}
