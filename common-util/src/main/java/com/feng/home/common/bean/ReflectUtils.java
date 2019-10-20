package com.feng.home.common.bean;


public class ReflectUtils {
    public static <T> T getSampleInstance(Class<T> tClass){
        try {
            return (T) tClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("无法初始化指定的类:" + tClass);
        }
    }
}
