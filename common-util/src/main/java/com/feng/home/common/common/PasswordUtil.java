package com.feng.home.common.common;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * 密码加密匹配工具类
 */
public class PasswordUtil {
    public static String encode(String passWord){
        String hashed = BCrypt.withDefaults().hashToString(10, passWord.toCharArray());
        return hashed;
    }

    public static boolean match(String password, String encPassword){
        return BCrypt.verifyer().verify(password.toCharArray(), encPassword.toCharArray()).verified;
    }
}
