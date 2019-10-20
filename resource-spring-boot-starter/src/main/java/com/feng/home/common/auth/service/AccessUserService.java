package com.feng.home.common.auth.service;

import com.feng.home.common.auth.bean.ContextUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

/**
 * 根据请求获取用户
 */
public interface AccessUserService {
    public Optional<ContextUser> accessUser(HttpServletRequest request);
}
