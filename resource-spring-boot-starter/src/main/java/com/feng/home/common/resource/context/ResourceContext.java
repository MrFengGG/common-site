package com.feng.home.common.resource.context;

import com.feng.home.common.resource.service.ResourceProcessor;
import com.feng.home.common.resource.annotation.ResourceClass;
import com.feng.home.common.resource.annotation.ResourceMeta;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResourceContext implements ApplicationContextAware {
    private static Map<String, ResourceMeta> resourceMetaMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //初始化所有资源
        Map<String, Object> resourceClass =  applicationContext.getBeansWithAnnotation(ResourceClass.class);
        resourceClass.keySet().forEach(this::initResourceFromObject);
        //处理资源
        Map<String, ResourceProcessor> resourceProcessMap = applicationContext.getBeansOfType(ResourceProcessor.class);
        Collection<ResourceMeta> resources = resourceMetaMap.values();
        resourceProcessMap.values().forEach(resourceProcess -> resourceProcess.processResourceMeta(resources));
    }

    public static Map getResourceMetaMap(){
        return new HashMap(resourceMetaMap);
    }

    private void initResourceFromObject(Object resourceObject){
        Method[] methods = resourceObject.getClass().getMethods();
        for(Method method : methods){
            ResourceMeta resourceMeta = AnnotationUtils.getAnnotation(method, ResourceMeta.class);
            if(resourceMeta != null){
                resourceMetaMap.put(resourceMeta.code(), resourceMeta);
            }
        }
    }
}
