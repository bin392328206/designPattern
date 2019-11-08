package com.atguigu.ct.weibo.design.pattern.creation.adapter.example;

public class SDCardImpl implements SDCard {
    @Override
    public void readSD() {
        System.out.println("正在读SD卡");
    }

    @Override
    public void writerSD() {
        System.out.println("正在写SD卡");
    }
}
