package com.atguigu.ct.weibo.design.pattern.creation.singleton;

/**
 *  懒汉式的双重检验
 *  首先还是私有构造方法
 */
public class SingletonDoubleCheck {

    private static volatile SingletonDoubleCheck singletonDoubleCheck;

    private SingletonDoubleCheck(){}


    public static SingletonDoubleCheck getInstance(){
        if (singletonDoubleCheck==null){
            synchronized (SingletonDoubleCheck.class){
                if (singletonDoubleCheck==null){
                    singletonDoubleCheck=new SingletonDoubleCheck();
                }
            }
        }

        return singletonDoubleCheck;
    }
}
