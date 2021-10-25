package com.feng.home.common.bean;

import com.feng.home.common.common.StringUtil;
import com.feng.home.common.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * create by FengZiyu
 * 2019/11/01
 * bean工具类
 */
public class BeanUtils {

    /**
     * 将驼峰命名转化为下划线
     * @param name
     * @return
     */
    public static String underscoreName(String name) {
        if (StringUtil.isEmpty(name)) {
            return "";
        }
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

    /**
     * 将下划线命名转化为驼峰命名
     * @param name
     * @return
     */
    public static String camelName(String name){
        if (StringUtil.isEmpty(name)) {
            return "";
        }
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

    /**
     * 将bean转化为下划线分隔Key组成的map
     * @param bean
     * @return
     */
    public static Map<String, Object> transBeanToMapWithUnderScore(Object bean){
        return transBeanToMap(bean, Boolean.TRUE);
    }

    /**
     * 将javaBean转化为map,key值转化为下划线分隔
     * @param underScore 是否转为下划线分隔,否则转化为驼峰
     * @return
     */
    public static Map<String, Object> transBeanToMap(Object bean, boolean underScore){
        return transBeanToMap(bean, underScore);
    }

    /**
     * 将javaBean转化为map,key值转化为下划线分隔,可通过exclude排除某些字段
     * @param bean
     * @param underScore 是否转为下划线分隔,否则转化为驼峰
     * @param exclude 要排除的字段
     * @return
     */
    public static Map<String, Object> transBeanToMap(Object bean, boolean underScore, String... exclude){
        Map<String, Object> queryMap = new HashMap<>();
        Set<String> excludeColumn = Arrays.stream(exclude).collect(Collectors.toSet());
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(NoConvertField.class) || excludeColumn.contains(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            Object value = ReflectUtils.getField(field, bean);
            if (value != null) {
                if(underScore) {
                    queryMap.put(underscoreName(field.getName()), value);
                }else{
                    queryMap.put(camelName(field.getName()), value);
                }
            }
        }
        return queryMap;
    }

    /**
     * 将map转化为bean
     * @param beanClass 要转化的对象类型
     * @param map
     * @param withUnderScore 是否转下划线
     * @param <T>
     * @return
     * @throws BusinessException
     */
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
            throw new BusinessException("对象转换异常" + map + e.getMessage());
        }
    }
}
