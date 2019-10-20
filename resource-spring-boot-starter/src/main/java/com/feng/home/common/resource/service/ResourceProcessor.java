package com.feng.home.common.resource.service;

import com.feng.home.common.resource.annotation.ResourceMeta;

import java.util.Collection;

public interface ResourceProcessor {
    public void processResourceMeta(Collection<ResourceMeta> resourceMetas);
}
