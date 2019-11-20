package com.feng.home.common.response;

import com.feng.home.common.common.StringUtil;
import com.feng.home.common.enums.ResponseCode;
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
                .code(ResponseCode.OK.getCode())
                .msg(ResponseCode.OK.getDesc()).data(result)
                .build();
    }

    public static FrontRequestResult fail(Object result, ResponseCode responseCode){
        return FrontRequestResult.builder()
                .code(responseCode.getCode())
                .msg(StringUtil.isEmpty(result) ? responseCode.getDesc() : String.valueOf(result))
                .build();
    }

    public static FrontRequestResult fail(){
        return fail(null, ResponseCode.FAIL);
    }

    public static FrontRequestResult fail(Object result){
        return fail(result, ResponseCode.FAIL);
    }
}
