package com.feng.home.common.support;

import lombok.Data;
/**
 * create by FengZiyu
 * 2019/12/30
 * 返回值
 */
@Data
public class OperateResult<T> {
    private T result;

    private boolean success;

    private String failReason;

    private OperateResult(T result, boolean success, String failReason) {
        this.result = result;
        this.success = success;
        this.failReason = failReason;
    }

    public static <T> OperateResult<T> ofSuccess(T t){
        return new OperateResult<T>(t, true, null);
    }

    public static <T> OperateResult<T> ofFail(T t, String msg){
        return new OperateResult<T>(t, false, msg);
    }

    public boolean isFail(){
        return !isSuccess();
    }
}
