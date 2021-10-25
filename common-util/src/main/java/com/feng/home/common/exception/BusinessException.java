package com.feng.home.common.exception;

import lombok.Getter;

/**
 * create by FengZiyu
 * 2019/11/20
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private Integer errorCode;

    public BusinessException(Integer errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
