package com.feng.home.common.response;

public enum ResultMsgEnum {
    SUCCESS(1, "成功"),
    FAIL(-1,"失败");
    private int code;

    private String msg;

    ResultMsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
