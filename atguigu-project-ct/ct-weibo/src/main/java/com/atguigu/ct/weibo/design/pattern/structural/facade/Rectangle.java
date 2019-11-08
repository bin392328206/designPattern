package com.atguigu.ct.weibo.design.pattern.structural.facade;

public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }
}