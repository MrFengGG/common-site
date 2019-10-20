package com.feng.home.common.collection;

import com.feng.home.common.common.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * create by FengZiyu
 * 2019/09/25
 */
public class Dict extends HashMap {

    public Dict(){
        super();
    }

    public Dict(Map map){
        super(map);
    }

    public Dict setIfNotNull(String key, String value){
        if(StringUtil.isEmpty(value)){
            this.put(key, value);
        }
        return this;
    }

    public Dict set(String key, Object value){
        this.put(key,value);
        return this;
    }

    public <V> V get(String key, Class<V> vClass){
        Object value = this.get(key);
        return value == null ? null : vClass.cast(value);
    }

    public <V> V getOrDefault(String key, Class<V> vClass, V defaultValue){
        Object value = this.get(key);
        return value == null ? defaultValue : vClass.cast(value);
    }

    public Integer getInt(String key, Integer defaultValue){
        Object value = this.get(key);
        return value == null ? defaultValue : Integer.parseInt(String.valueOf(value));
    }

    public Integer getInt(String key){
        return getInt(key, null);
    }

    public String getStr(String key, String defaultValue){
        Object value = this.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    public Boolean getBoolean(String key, Boolean defaultValue){
        String value = this.getStr(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public Boolean getBoolean(String key){
        return getBoolean(key, null);
    }

    public <T> Collection<T> getCollection(String key){
        return (Collection<T>) this.get(key);
    }

    public String getStr(String key){
        return getStr(key, null);
    }
}
