package com.feng.home.common.common;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * create by FengZiyu
 * 2019/09/03
 * 枚举工具类
 */
public class EnumUtils {
    public static <T extends Enum> T[] getAllValues(Class<T> tClass){
        if(!tClass.isEnum()){
            return null;
        }
        return tClass.getEnumConstants();
    }

    public static <T extends Enum> Optional<T> getValue(Class<T> tClass, Predicate<T> predicate){
        return Stream.of(tClass.getEnumConstants()).filter(predicate).findAny();
    }
}
