package com.minis.testService.Impl;

import com.minis.testService.IAction;

public class Action1 implements IAction {
    @Override
    public void doAction() {
        System.out.println("real do action1...");
    }
}
