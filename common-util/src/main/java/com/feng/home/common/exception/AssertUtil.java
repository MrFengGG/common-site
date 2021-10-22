package com.feng.home.common.exception;

import com.feng.home.common.common.StringUtil;

public class AssertUtil {
    public static void assertTrue(boolean flag, String message){
        if(!flag){
            throw new SampleBusinessException(message);
        }
    }

    public static void assertFalse(boolean value, String message){
        assertTrue(!value, message);
    }

    public static void assertNotNull(Object object, String message){
        if(object == null){
            throw new SampleBusinessException(message);
        }
    }

    public static void assertNotEmpty(String value, String message){
        assertTrue(StringUtil.isNotEmpty(value), message);
    }
}
