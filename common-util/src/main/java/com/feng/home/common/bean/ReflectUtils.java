package com.feng.home.common.bean;

import java.lang.reflect.Field;


public class ReflectUtils {
    public static <T> T getSampleInstance(Class<T> tClass){
        try {
            return (T) tClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("无法初始化指定的类:" + tClass);
        }
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException var3) {
            throw new IllegalStateException("Unexpected reflection exception - " + var3.getClass().getName() + ": " + var3.getMessage());
        }
    }
}
