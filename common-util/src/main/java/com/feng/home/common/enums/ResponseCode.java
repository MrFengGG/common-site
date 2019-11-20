package com.feng.home.common.enums;

/**
 * create by FengZiyu
 * 2019/11/20
 */
public enum ResponseCode {
    OK(200,"成功"),
    FAIL(-1,"失败"),
    PERMISSION_DENIED(403,"权限不足"),
    INVALID_TOKEN(400,"无效的令牌"),
    NEED_LOGIN(401, "未登陆");

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    int code;
    String desc;
}
