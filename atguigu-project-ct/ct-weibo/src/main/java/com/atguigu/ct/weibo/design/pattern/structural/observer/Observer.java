package com.atguigu.ct.weibo.design.pattern.structural.observer;

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}