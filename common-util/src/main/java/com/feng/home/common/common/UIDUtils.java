package com.feng.home.common.common;

import java.util.UUID;

public class UIDUtils {
    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }
}
