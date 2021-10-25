package com.feng.home.common.collection;

/**
 * 集合工具类
 */
public class CollectionUtils {
    /**
     * 安全获取数组索引值
     * @param array
     * @param index
     * @param <T>
     * @return
     */
    public static<T> T safeGet(T[] array, Integer index){
        if(array == null || array.length <= index){
            return null;
        }
        return array[index];
    }

}
