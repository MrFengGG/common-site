package com.feng.home.common.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create by FengZiyu
 * 2019/09/03
 */
public class StringUtil {
    public static boolean isEmpty(String value){
        return value == null || value.equals("");
    }

    public static boolean isEmpty(Object value){
        return value == null || String.valueOf(value).equals("");
    }

    public static boolean isAllNotEmpty(Object ...value){
        for(Object val : value){
            if(StringUtil.isEmpty(val)){
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(Object value){
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(String value){
        return !isEmpty(value);
    }

    public static String join(String[] strings, String joiner){
        return String.join(joiner, strings);
    }

    public static Object join(Object[] objects, String joiner){
        return Arrays.stream(objects).map(String::valueOf).collect(Collectors.joining(joiner));
    }

    public static String join(Collection strings, String joiner){
        return ((Collection<Object>) strings).stream().map(String::valueOf).collect(Collectors.joining(joiner));
    }

    public static Collection<String> split(String str, String splitter){
        return Stream.of(str.split(splitter)).collect(Collectors.toList());
    }

    public static String getEmptyParams(int length){
        List<String> emptyParams = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            emptyParams.add("?");
        }
        return join(emptyParams, ",");
    }
}
