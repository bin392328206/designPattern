package com.atguigu.ct.weibo.design.pattern.behavioral.visitor;

public class Keyboard  implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}
