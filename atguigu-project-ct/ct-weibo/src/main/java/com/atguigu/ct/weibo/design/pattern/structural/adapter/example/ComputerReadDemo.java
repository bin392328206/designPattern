package com.atguigu.ct.weibo.design.pattern.structural.adapter.example;

public class ComputerReadDemo {
    public static void main(String[] args) {
        //正常流程
        Computer computer=new ThinkpadComputer();
        SDCard sdCard = new SDCardImpl();
        computer.readSD(sdCard);
        System.out.println("--------------------------------");
        //适配器流程
        SDAdapterTF sdAdapterTF = new SDAdapterTF(new TFCardImpl());
        computer.readSD(sdAdapterTF);


    }


}
