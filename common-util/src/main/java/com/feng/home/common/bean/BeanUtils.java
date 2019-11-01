package com.feng.home.common.bean;

import com.feng.home.common.common.StringUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * create by FengZiyu
 * 2019/11/01
 */
public class BeanUtils {
    /**
     * 将驼峰命名转化为下划线命名
     * @param name
     * @return
     */
    public static String underscoreName(String name) {
        if (!StringUtil.isEmpty(name)) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            result.append(name.substring(0, 1).toLowerCase());
            for(int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                String slc = s.toLowerCase();
                if (!s.equals(slc)) {
                    result.append("_").append(slc);
                } else {
                    result.append(s);
                }
            }
            return result.toString();
        }
    }

    /**
     * 将bean转化为查询字段
     * @param bean
     * @return
     */
    public static Map<String, Object> transBeanToMap(Object bean){
        Map<String, Object> queryMap = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(NoConvertField.class)) {
                field.setAccessible(true);
                Object value = ReflectUtils.getField(field, bean);
                if (value != null) {
                    queryMap.put(underscoreName(field.getName()), value);
                }
            }
        }
        return queryMap;
    }

    public static Map<String, Object> transBeanToMap(Object bean, String... exclude){
        Map<String, Object> queryMap = new HashMap<>();
        Set<String> excludeColumn = Arrays.stream(exclude).collect(Collectors.toSet());
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(NoConvertField.class) || excludeColumn.contains(field.getName())) {
                field.setAccessible(true);
                Object value = ReflectUtils.getField(field, bean);
                if (value != null) {
                    queryMap.put(underscoreName(field.getName()), value);
                }
            }
        }
        return queryMap;
    }
}
