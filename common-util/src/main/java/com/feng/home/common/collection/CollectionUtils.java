package com.feng.home.common.collection;

public class CollectionUtils {
    public static<T> T safeGet(T[] array, Integer index){
        if(array == null || array.length <= index){
            return null;
        }
        return array[index];
    }

}
