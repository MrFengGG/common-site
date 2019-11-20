package com.feng.home.common.auth.service;

import com.feng.home.common.auth.bean.ContextUser;
import com.feng.home.common.exception.AuthException;
import com.feng.home.common.exception.ForbiddenException;
import com.feng.home.common.exception.InvalidTokenException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * token校验
 */
public interface AccessUserService {
    public Optional<ContextUser> accessUser(HttpServletRequest request) throws ForbiddenException, AuthException, InvalidTokenException;
}
