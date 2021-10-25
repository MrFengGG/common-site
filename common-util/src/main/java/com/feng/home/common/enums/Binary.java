package com.feng.home.common.enums;

/**
 * 二元枚举
 */
public enum Binary {
    YES(1, "是"), NO(0, "否");
    private Integer value;
    private String title;

    Binary(Integer value, String title) {
        this.value = value;
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
