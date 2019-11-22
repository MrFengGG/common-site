package com.feng.home.common.exception;

import com.feng.home.common.enums.ResponseCode;

/**
 * create by FengZiyu
 * 2019/11/21
 */
public class SampleBusinessException extends BusinessException {
    @Override
    public ResponseCode getResponseCode() {
        return ResponseCode.FAIL;
    }

    public SampleBusinessException(String message) {
        super(message);
    }
}
