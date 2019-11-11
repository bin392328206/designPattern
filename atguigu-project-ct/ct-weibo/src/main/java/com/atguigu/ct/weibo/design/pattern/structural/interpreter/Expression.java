package com.atguigu.ct.weibo.design.pattern.structural.interpreter;

public interface Expression {
    public boolean interpret(String context);
}