package com.feng.home.common.common;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create by FengZiyu
 * 2019/09/03
 * 字符串工具类
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

    /**
     * 将数组聚合为字符串
     * @param strings
     * @param joiner
     * @return
     */
    public static String join(String[] strings, String joiner){
        if(strings == null){
            return null;
        }
        return String.join(joiner, strings);
    }

    /**
     * 将集合聚合为字符串
     * @param strings
     * @param joiner
     * @return
     */
    public static<T> String join(Collection<T> strings, String joiner){
        if(CollectionUtils.isEmpty(strings)){
            return null;
        }
        return strings.stream().map(String::valueOf).collect(Collectors.joining(joiner));
    }

    /**
     * 分割字符串
     * @param str
     * @param splitter
     * @return
     */
    public static Collection<String> split(String str, String splitter){
        if(isEmpty(str)){
            return null;
        }
        return Stream.of(str.split(splitter)).collect(Collectors.toList());
    }

    /**
     * 获取sql占位符
     * @param length
     * @return
     */
    public static String getEmptyParams(int length){
        List<String> emptyParams = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            emptyParams.add("?");
        }
        return join(emptyParams, ",");
    }
}
