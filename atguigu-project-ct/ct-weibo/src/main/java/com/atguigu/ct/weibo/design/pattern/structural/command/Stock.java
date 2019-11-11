package com.atguigu.ct.weibo.design.pattern.structural.command;

public class Stock {

    private String name = "ABC";
    private int quantity = 10;

    public void buy(){
        System.out.println("买东西");
    }
    public void sell(){
        System.out.println("卖东西");
    }
}
