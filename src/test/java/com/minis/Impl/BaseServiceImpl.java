package com.minis.Impl;

import com.minis.AService;
import com.minis.BaseService;
import com.minis.BasebaseService;
import com.minis.beans.factory.annotation.Autowired;
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
