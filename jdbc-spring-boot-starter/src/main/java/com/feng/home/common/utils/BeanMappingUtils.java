package com.feng.home.common.utils;

import com.feng.home.common.resource.ModelMapping;
import com.feng.home.common.resource.NoDbField;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanMappingUtils {
    /**
     * 将驼峰命名转化为下划线命名
     * @param name
     * @return
     */
    public static String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
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
    public static Map<String, Object> transBeanToQueryMap(Object bean){
        Map<String, Object> queryMap = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(NoDbField.class)) {
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, bean);
                if (value != null) {
                    queryMap.put(underscoreName(field.getName()), value);
                }
            }
        }
        return queryMap;
    }

    public static ModelMapping getModelMapping(Object object){
        return getAnnotation(object, ModelMapping.class);
    }

    public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotationClass){
        return object.getClass().getAnnotation(annotationClass);
    }
}
