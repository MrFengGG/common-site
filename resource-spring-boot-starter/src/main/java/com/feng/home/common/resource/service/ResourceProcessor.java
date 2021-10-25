package com.feng.home.common.resource.service;

import com.feng.home.common.resource.annotation.ResourceMeta;

import java.util.Collection;

/**
 * 资源初始化完成时回调
 */
public interface ResourceProcessor {
    public void processResourceMeta(Collection<ResourceMeta> resourceMetas);
}
