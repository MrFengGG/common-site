package com.feng.home.common.auth;

import com.feng.home.common.auth.bean.ContextUser;

public class AuthContext {
    private static ThreadLocal<ContextUser> contextUserThreadLocal = new ThreadLocal<>();

    public static void setContextUser(ContextUser contextUser){
        contextUserThreadLocal.set(contextUser);
    }

    public static ContextUser getContextUser(){
        return contextUserThreadLocal.get();
    }

    public static void clean(){
        contextUserThreadLocal.remove();
    }
}
