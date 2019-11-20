package com.feng.home.common.exception;

import com.feng.home.common.enums.ResponseCode;

/**
 * create by FengZiyu
 * 2019/11/20
 */
public class AuthException extends BusinessException {
    public AuthException(){
        super();
    }

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.NEED_LOGIN;
    }
}
