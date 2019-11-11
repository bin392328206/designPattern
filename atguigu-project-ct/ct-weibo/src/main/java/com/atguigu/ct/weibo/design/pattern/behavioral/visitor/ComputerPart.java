package com.atguigu.ct.weibo.design.pattern.behavioral.visitor;

public interface ComputerPart {
    public void accept(ComputerPartVisitor computerPartVisitor);
}