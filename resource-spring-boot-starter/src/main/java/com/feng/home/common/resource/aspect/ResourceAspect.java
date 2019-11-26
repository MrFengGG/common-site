package com.feng.home.common.resource.aspect;

import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.auth.bean.ContextUser;
import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.auth.AuthContext;
import com.feng.home.common.auth.service.AuthenticateService;
import com.feng.home.common.exception.AuthException;
import com.feng.home.common.exception.ForbiddenException;
import com.feng.home.common.exception.InvalidTokenException;
import com.feng.home.common.resource.annotation.ResourceMeta;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Aspect
public class ResourceAspect {

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private AccessUserService accessUserService;

    @Autowired
    private ResourceConfiguration resourceConfiguration;

    @Pointcut("@annotation(com.feng.home.common.resource.annotation.ResourceMeta)")
    public void addAdvice(){}

    @Around("addAdvice()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        //获取当前访问的资源
        ResourceMeta resourceMeta = AnnotationUtils.getAnnotation(((MethodSignature)pjp.getSignature()).getMethod(), ResourceMeta.class);
        if(!resourceMeta.enableAuthCheck()){
            return pjp.proceed();
        }
        //获取当前访问的用户
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        Optional<ContextUser> contextUserOptional = accessUserService.accessUser(request);
        //验证是否登陆
        if(!contextUserOptional.isPresent()){
            if(this.resourceConfiguration.isEnableRedirect()){
                HttpServletResponse response = requestAttributes.getResponse();
                redirectLogin(request, response);
            }
            throw new AuthException();
        }
        //过滤白名单,验证权限
        Collection<String> userRoles = contextUserOptional.map(ContextUser::getRoleList).orElse(resourceConfiguration.getVisitorRoleList());
        if(resourceMeta.enableAuthCheck() && !userRoles.stream().anyMatch(role -> resourceConfiguration.getWhiteListRole().contains(role))){
            checkAuth(resourceMeta, userRoles);
        }
        AuthContext.setContextUser(ContextUser.builder().roleList(userRoles).username(contextUserOptional.map(ContextUser::getUsername).orElse(null)).build());
        return pjp.proceed();
    }

    private void checkAuth(ResourceMeta resourceMeta, Collection<String> roles) throws IOException, ForbiddenException, AuthException, InvalidTokenException {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        if(!authenticateService.authenticate(resourceMeta, roles)){
            throw new ForbiddenException("权限不足");
        }
    }

    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(resourceConfiguration.getLoginUrl() + "?originUrl=" + request.getRequestURI());
    }
}
