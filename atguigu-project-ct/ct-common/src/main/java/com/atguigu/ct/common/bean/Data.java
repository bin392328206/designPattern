package com.atguigu.ct.common.bean;

public abstract class Data  implements Val {
    public String content;

    public void setValue(Object value) {
        this.content=(String) value;
    }

    public Object getValue() {
        return content;
    }
}
