package com.feng.home.common.auth.service;

import com.feng.home.common.resource.annotation.ResourceMeta;

import java.util.Collection;

/**
 * 权限认证
 */
public interface AuthenticateService {
    public boolean authenticate(ResourceMeta resourceMeta, Collection<String> roleList);
}
