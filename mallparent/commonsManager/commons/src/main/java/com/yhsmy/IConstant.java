package com.yhsmy;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * @auth 李正义
 * @date 2019/11/7 11:17
 **/
public class IConstant {

    /** 全局默认会话时间 **/
    public static final Long GLOBAL_SESSION_TIMEOUT = 3600000L;

    /** 记住我的默认时间，1天，单位秒 **/
    public static final int GLOBAL_COOKIE_TIMEOUT = 86400;

    /** redis经过FastJson序列化时接受的前缀 **/
    public static final String GLOBAL_INSTACE = "com.yhsmy.*";
    public static final String GLOBAL_SHIRO_PREFIX = "shiro_redis_*";
    public static final String COOKIE_NAME = "yhsmy.name";
    public static final String COOKIE_VALUE = "/";

    /** shiro的cache前缀 **/
    public static final String SHIRO_CACHE_PREFIX = "shiro_redis_cache_";

    /** shiro当前用户的凭证 **/
    public static final String CURRENTPRINCIPAL = "currentPrincipal";

    /** Shiro密码加密算法 **/
    public static final String SHIRO_SCCRITY = "SHA-1";

    /** shiro密码加密次数 **/
    public static final int SHIRO_ITEAROTR = 3;

    /** 手机号正则 **/
    public static final Pattern MOBILE_PATTERN = Pattern.compile (IConstant.MOBILE);
   // public static final String MOBILE = "^[1](([3|5|8][\\\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\\\d]{8}$";
    public static final String MOBILE = "^1[3|4|5|7|8][0-9]{9}$";


    /** 邮箱正则 **/
    public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile (IConstant.EMAIL);

    public static final String MALL_URL = "https://www.yhsmy.com";


}
