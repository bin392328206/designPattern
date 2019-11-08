package com.atguigu.ct.weibo.design.pattern.structural.adapter.example;


/**
 * 创建一个适配器 让SD 兼容TF
 */
public class SDAdapterTF  implements  SDCard{

    private TFCard tfCard;

    public SDAdapterTF(TFCard tfCard) {
        this.tfCard = tfCard;
    }

    @Override
    public void readSD() {
        System.out.println("adapter read of tf ");
        tfCard.readTF();
    }

    @Override
    public void writerSD() {
        System.out.println("adapter write tf ");
        tfCard.writeTF();
    }
}
