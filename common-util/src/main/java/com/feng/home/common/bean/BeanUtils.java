package com.feng.home.common.bean;

import com.feng.home.common.common.StringUtil;
import com.feng.home.common.exception.BusinessException;
import com.feng.home.common.exception.SampleBusinessException;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    //将驼峰命名转化为下划线命名
    public static String underscoreName(String name) {
        if (StringUtil.isEmpty(name)) {
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

    public static String camelName(String name){
        if (StringUtil.isEmpty(name)) {
            return "";
        }else{
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals("_")) {
                    result.append(name.substring(i+1, i + 2).toUpperCase());
                } else {
                    result.append(s);
                }
            }
            return result.toString();
        }
    }

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

    public static <T> T transMapToBean(Class<T> beanClass, Map<String, ? extends Object> map, boolean withUnderScore) throws BusinessException {
        if(map == null){
            return null;
        }
        try {
            Object object = beanClass.newInstance();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String fieldName = field.getName();
                if(withUnderScore){
                    fieldName = underscoreName(fieldName);
                }
                field.set(object, map.get(fieldName));
            }
            return (T) object;
        } catch (Exception e) {
            throw new SampleBusinessException("对象转换异常" + map + e.getMessage());
        }
    }
}
