package com.atguigu.ct.weibo.design.pattern.creation.prototype;

import javax.xml.transform.Source;

public class Rectangle extends Shape {

    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }

    @Override
    public void say() {
        System.out.println("hello son");
    }
}