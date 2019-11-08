package com.atguigu.ct.weibo.design.pattern.creation.singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举方式 创建单例
 */
public enum Singleton {
    INSTANCE;

    static String  a="a";
    public void whateverMethod(){
        System.out.println("1111");
    }

    static {
        List<String> list=new ArrayList<>();
        list.add(a);
        System.out.println(list.size());
    }
}
