package com.feng.home.common.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class FrontRequestResult<T> implements Serializable {
    private T data;

    private int code;

    private String msg;

    public static FrontRequestResult success(Object result){
        return FrontRequestResult.builder()
                .code(ResultMsgEnum.SUCCESS.getCode())
                .msg(ResultMsgEnum.SUCCESS.getMsg()).data(result)
                .build();
    }

    public static FrontRequestResult success(){
        return success(null);
    }

    public static FrontRequestResult fail(Object result){
        return FrontRequestResult.builder()
                .code(ResultMsgEnum.FAIL.getCode())
                .msg(ResultMsgEnum.FAIL.getMsg()).data(result)
                .build();
    }

    public static FrontRequestResult fail(){
        return fail(null);
    }
}
