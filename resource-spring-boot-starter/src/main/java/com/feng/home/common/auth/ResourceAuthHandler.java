package com.feng.home.common.auth;

import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.auth.bean.ContextUser;
import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.auth.service.AuthenticateService;
import com.feng.home.common.exception.AuthException;
import com.feng.home.common.exception.BusinessException;
import com.feng.home.common.exception.ForbiddenException;
import com.feng.home.common.exception.InvalidTokenException;
import com.feng.home.common.resource.annotation.ResourceMeta;
import com.feng.home.common.resource.base.ResourceHandler;
import com.feng.home.common.support.OperateResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * create by FengZiyu
 * 2019/12/30
 */
public class ResourceAuthHandler implements ResourceHandler {
    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private AccessUserService accessUserService;

    @Autowired
    private ResourceConfiguration resourceConfiguration;

    @Override
    public void handBefore(ResourceMeta resourceMeta, ProceedingJoinPoint joinPoint) throws BusinessException {
        if(resourceMeta.enableAuthCheck()){
            //获取当前访问的用户
            ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            HttpServletRequest request = requestAttributes.getRequest();
            Optional<ContextUser> contextUserOptional = accessUserService.accessUser(request);

            //验证是否登陆
            if(!contextUserOptional.isPresent()){
                if(this.resourceConfiguration.isEnableRedirect()){
                    HttpServletResponse response = requestAttributes.getResponse();
                    try {
                        redirectLogin(request, response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                throw new AuthException();
            }
            //过滤白名单,验证权限
            Collection<String> userRoles = contextUserOptional.map(ContextUser::getRoleList).orElse(resourceConfiguration.getVisitorRoleList());
            if(resourceMeta.enableAuthCheck() && userRoles.stream().noneMatch(role -> resourceConfiguration.getWhiteListRole().contains(role))){
                checkAuth(resourceMeta, userRoles);
            }
            ContextUser contextUser = ContextUser.builder().roleList(userRoles).username(contextUserOptional.map(ContextUser::getUsername)
                    .orElse(null)).extend(contextUserOptional.map(ContextUser::getExtend).orElse(null)).build();
            AuthContext.setContextUser(contextUser);
        }
    }

    @Override
    public void handAfter(ResourceMeta resourceMeta, Object result) throws BusinessException {

    }

    @Override
    public Long getPriority() {
        return Long.MAX_VALUE;
    }
    private void checkAuth(ResourceMeta resourceMeta, Collection<String> roles) throws ForbiddenException, AuthException, InvalidTokenException {
        if(!authenticateService.authenticate(resourceMeta, roles)){
            throw new ForbiddenException("权限不足");
        }
    }
    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(resourceConfiguration.getLoginUrl() + "?originUrl=" + request.getRequestURI());
    }
}
