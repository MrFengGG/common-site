package com.feng.home.common.auth.service.impl;

import com.feng.home.common.auth.service.AuthenticateService;
import com.feng.home.common.resource.annotation.ResourceMeta;

import java.util.Collection;

public class HttpAuthenticateService implements AuthenticateService {

    @Override
    public boolean authenticate(ResourceMeta resourceMeta, Collection<String> roleList) {
        return false;
    }
}
