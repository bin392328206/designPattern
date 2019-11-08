package com.atguigu.ct.weibo.design.pattern.creation.adapter.example;


public class TFCardImpl implements TFCard {

    @Override
    public void readTF() {
        System.out.println("正在读TF卡");
    }

    @Override
    public void writeTF() {
        System.out.println("正在写TF卡");
    }
}
