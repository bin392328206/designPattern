package com.atguigu.ct.weibo.design.pattern.structural.adapter.example;

public class ThinkpadComputer implements Computer {
    @Override
    public void readSD(SDCard sdCard) {
        if (null==sdCard)throw new NullPointerException("SDCard is not nll");
        sdCard.readSD();
    }
//
//    /**
//     * 这种接口不同用 毕竟不是所有的电脑都 可以读 TF的 但是所有的电脑可以读SD
//     * @param tfCard
//     */
//    @Override
//    public void readTF(TFCard tfCard) {
//        if (null==tfCard)throw new NullPointerException("TFCard is not nll");
//        tfCard.readTF();
//    }
}
