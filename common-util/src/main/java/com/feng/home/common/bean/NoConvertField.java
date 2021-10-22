package com.feng.home.common.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * create by FengZiyu
 * 2019/11/01
 * 标识bean中不需要转化的字段
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoConvertField {
}
