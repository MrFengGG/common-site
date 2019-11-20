package com.feng.home.common.exception;

import com.feng.home.common.enums.ResponseCode;

/**
 * create by FengZiyu
 * 2019/11/20
 */
public class InvalidTokenException extends BusinessException{
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.INVALID_TOKEN;
    }
}
