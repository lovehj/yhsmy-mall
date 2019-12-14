package com.yhsmy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * 验证码
 *
 * @auth 李正义
 * @date 2019/11/6 22:43
 **/
public class VerifyCodeUtil {

    /**
     * 这里去掉了1,0,i,o这几个容易混淆的字符
     */
    public static final String VERIFY_CODE = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private static Random random = new Random ();

    /**
     * 生成一个乘积的验证码
     *
     * @return
     */
    public static Verify generateVerify () {
        int num1 = new Random ().nextInt (20) + 1;
        int num2 = new Random ().nextInt (20) + 1;
        return new Verify (num1 + " x " + num2, num1 * num2);
    }

    /**
     * 生成一个指定长度，指定字符源的验证码
     *
     * @param verifySize 生成验证码的长度
     * @param source     生成验证码的字符源
     * @return
     */
    public static String generateVerify (int verifySize, String source) {
        if (verifySize <= 0) {
            verifySize = 1;
        }

        if (StringUtils.isEmpty (source)) {
            source = VERIFY_CODE;
        }

        int sourceLen = source.length ();
        Random rand = new Random (System.currentTimeMillis ());
        StringBuilder builder = new StringBuilder (verifySize);
        for (int i = 0; i < verifySize; i++) {
            builder.append (source.charAt (rand.nextInt (sourceLen - 1)));
        }
        return builder.toString ();
    }




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Verify {
        private String code;
        private Integer value;
    }
}
