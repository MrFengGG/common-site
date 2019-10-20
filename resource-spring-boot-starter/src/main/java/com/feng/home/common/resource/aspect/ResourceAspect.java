package com.feng.home.common.resource.aspect;

import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.auth.bean.ContextUser;
import com.feng.home.common.auth.service.AccessUserService;
import com.feng.home.common.auth.AuthContext;
import com.feng.home.common.auth.service.AuthenticateService;
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
        ResourceMeta resourceMeta = AnnotationUtils.getAnnotation(((MethodSignature)pjp.getSignature()).getMethod(), ResourceMeta.class);
        if(resourceMeta.enableAuthCheck()){
            checkAuth(resourceMeta);
        }
        Object result = pjp.proceed();

        return result;
    }

    private void checkAuth(ResourceMeta resourceMeta) throws IOException {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        Optional<ContextUser> contextUserOptional = accessUserService.accessUser(request);
        if(contextUserOptional.isPresent()){
            ContextUser contextUser = contextUserOptional.get();
            AuthContext.setContextUser(contextUser);
            if(!authenticateService.authenticate(resourceMeta, contextUser.getRoleList())){
                //如果验证未通过且没有任何权限,重定向到登录路径
               if(contextUser.getRoleList().size() <= 0){
                   redirect(request, response);
               }
               throw new UnsupportedOperationException("权限不足");
            }
        }
        redirect(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(resourceConfiguration.getLoginUrl() + "?originUrl=" + request.getRequestURI());
    }
}
