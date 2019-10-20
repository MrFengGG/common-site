package com.feng.home.common.common;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * create by FengZiyu
 * 2019/09/03
 */
public class EnumUtils {
    public static <T> T[] getAllValues(Class<T> tClass){
        if(!tClass.isEnum()){
            return null;
        }
        return tClass.getEnumConstants();
    }

    public static <T> T getValue(Class<T> tClass, Predicate<T> predicate){
        if(!tClass.isEnum()){
            return null;
        }
        return Stream.of(tClass.getEnumConstants()).filter(predicate).findAny().orElse(null);
    }
}
