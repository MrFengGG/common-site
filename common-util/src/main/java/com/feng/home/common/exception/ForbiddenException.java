package com.feng.home.common.exception;

import com.feng.home.common.enums.ResponseCode;

/**
 * create by FengZiyu
 * 2019/11/20
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.PERMISSION_DENIED;
    }
}
