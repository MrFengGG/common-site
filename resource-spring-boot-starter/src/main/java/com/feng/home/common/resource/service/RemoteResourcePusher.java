package com.feng.home.common.resource.service;

import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.resource.annotation.ResourceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component
public class RemoteResourcePusher implements ResourceProcessor {
    @Resource
    private ResourceConfiguration resourceConfiguration;

    @Override
    public void processResourceMeta(Collection<ResourceMeta> resourceMetas) {

    }
}
