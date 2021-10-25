package com.feng.home.common.common;

import java.util.UUID;

/**
 * UUID生成工具类
 */
public class UIDUtils {
    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }
}
