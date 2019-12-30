package com.feng.home.common.resource.aspect;

import com.feng.home.common.resource.annotation.ResourceMeta;
import com.feng.home.common.resource.base.ResourceHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

@Aspect
public class ResourceAspect {

    Map<Long, ResourceHandler> handlerMap = new TreeMap<>(Comparator.reverseOrder());

    public void addHandle(ResourceHandler handler){
        handlerMap.put(handler.getPriority(), handler);
    }

    @Pointcut("@annotation(com.feng.home.common.resource.annotation.ResourceMeta)")
    public void addAdvice(){}

    @Around("addAdvice()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        //获取当前访问的资源
        ResourceMeta resourceMeta = AnnotationUtils.getAnnotation(((MethodSignature)pjp.getSignature()).getMethod(), ResourceMeta.class);
        handlerMap.values().forEach(handler -> handler.handBefore(resourceMeta, pjp));
        Object result =  pjp.proceed();
        handlerMap.values().forEach(handler -> handler.handAfter(resourceMeta, result));
        return result;
    }
}
