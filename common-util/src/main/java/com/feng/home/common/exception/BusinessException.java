package com.feng.home.common.exception;

import com.feng.home.common.enums.ResponseCode;

/**
 * create by FengZiyu
 * 2019/11/20
 */
public abstract class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ResponseCode getResponseCode();
}
