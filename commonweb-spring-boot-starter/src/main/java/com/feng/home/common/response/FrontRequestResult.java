package com.feng.home.common.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class FrontRequestResult<T> implements Serializable {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    private static final String DEFAULT_FAIL_MESSAGE = "FAIL_MESSAGE";

    private T data;

    private int code;

    private boolean success;

    private String msg;

    public static<T> FrontRequestResult<T> success(T data){
        return FrontRequestResult.<T>builder()
                .code(ResponseCode.SUCCESS)
                .msg(DEFAULT_SUCCESS_MESSAGE)
                .data(data)
                .build();
    }

    public static FrontRequestResult<Object> fail(Integer responseCode, String errorMsg){
        return FrontRequestResult.builder()
                .code(responseCode)
                .msg(errorMsg)
                .build();
    }
}
