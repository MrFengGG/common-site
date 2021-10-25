package com.feng.home.common.resource.handler;

import com.feng.home.common.exception.BusinessException;
import com.feng.home.common.resource.annotation.ResourceMeta;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * create by FengZiyu
 * 2019/12/30
 */
public interface ResourceHandler {
    public void handBefore(ResourceMeta resourceMeta, ProceedingJoinPoint joinPoint) throws BusinessException;

    public void handAfter(ResourceMeta resourceMeta, Object result) throws BusinessException;

    public Long getPriority();
}
