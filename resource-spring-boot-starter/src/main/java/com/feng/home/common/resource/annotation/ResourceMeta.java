package com.feng.home.common.resource.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceMeta {
    String code();

    String resourceName();

    String resourceDesc() default "";

    String url() default "";

    String group();

    boolean enableAuthCheck() default true;
}
