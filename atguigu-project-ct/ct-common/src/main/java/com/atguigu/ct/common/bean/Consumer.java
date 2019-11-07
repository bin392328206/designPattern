package com.atguigu.ct.common.bean;

import java.io.Closeable;

/**
 * 消费者接口
 */
public interface Consumer extends Closeable {
    void consume();
}
