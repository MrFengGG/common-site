package com.feng.home.common.resource.aspect;

import com.feng.home.common.resource.annotation.ResourceMeta;
import com.feng.home.common.resource.base.ResourceHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import java.util.LinkedList;

@Aspect
public class ResourceAspect {

    private LinkedList<ResourceHandler> handlerList = new LinkedList<>();

    public void addHandle(ResourceHandler handler){
        handlerList.addLast(handler);
    }

    @Pointcut("@annotation(com.feng.home.common.resource.annotation.ResourceMeta)")
    public void addAdvice(){}

    @Around("addAdvice()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        //获取当前访问的资源
        ResourceMeta resourceMeta = AnnotationUtils.getAnnotation(((MethodSignature)pjp.getSignature()).getMethod(), ResourceMeta.class);
        handlerList.forEach(handler -> handler.handBefore(resourceMeta, pjp));
        Object result =  pjp.proceed();
        handlerList.forEach(handler -> handler.handAfter(resourceMeta, result));
        return result;
    }
}
