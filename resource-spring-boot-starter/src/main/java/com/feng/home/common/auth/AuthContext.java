package com.feng.home.common.auth;

import com.feng.home.common.ResourceConfiguration;
import com.feng.home.common.auth.bean.ContextUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

public class AuthContext implements ApplicationContextAware {
    private static ThreadLocal<ContextUser> contextUserThreadLocal = new ThreadLocal<>();

    private static ResourceConfiguration resourceConfiguration;

    public static void setContextUser(ContextUser contextUser){
        contextUserThreadLocal.set(contextUser);
    }

    public static ContextUser getContextUser(){
        return contextUserThreadLocal.get();
    }

    public static void clean(){
        contextUserThreadLocal.remove();
    }

    public static boolean currentUserInWhiteList(){
        Set<String> whiteListRole = resourceConfiguration.getWhiteListRole();
        Collection<String> roleList = getContextUser().getRoleList();
        return roleList.stream().anyMatch(whiteListRole::contains);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        resourceConfiguration = applicationContext.getBean(ResourceConfiguration.class);
    }
}