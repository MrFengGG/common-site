package com.feng.home.common.validate;

import com.feng.home.common.exception.SampleBusinessException;

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
}
