package com.minis.testService.Impl;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.testService.BaseService;
import com.minis.testService.BasebaseService;
import lombok.Setter;

@Setter
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BasebaseService basebaseService;

    @Override
    public void test() {
        System.out.println("----------------");
        basebaseService.test();
        System.out.println("----------------");
    }
}
