package com.yhsmy.utils;

import java.util.UUID;

/**
 * 生成UUID
 *
 * @auth 李正义
 * @date 2019/11/6 21:48
 **/
public class UUIDUtil {

    /**
     * 生成一个32位，不带"-"的UUID字符串
     * @return
     */
    synchronized public static String generateUUID() {
       return UUID.randomUUID ().toString ().replace ("-", "");
    }

}
