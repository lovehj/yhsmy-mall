package com.yhsmy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @auth 李正义
 * @date 2019/11/29 10:30
 **/
public class DateTimeUtil {

    /**
     * 年-月-日
     **/
    public static final String FORMAT_DATA = "yyyy-MM-dd";

    /**
     * 时:分:秒
     **/
    public static final String FORMAT_TIME = "HH:mm:ss";

    /**
     * 年-月-日 时:分:秒
     **/
    public static final String FORMMAT_FULL = FORMAT_DATA + " " + FORMAT_TIME;


    /**
     * LocalDateTime转String
     *
     * @param localDateTime
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String localDateTimeToStr (LocalDateTime localDateTime) {
        return DateTimeUtil.localDateTimeToStr (localDateTime, null);
    }

    /**
     * LocalDateTime转String 按指定是的日期格式
     *
     * @param localDateTime
     * @param pattern       指定是的日期格式
     * @return
     */
    public static String localDateTimeToStr (LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return "";
        }
        if (StringUtils.isEmpty (pattern)) {
            pattern = FORMMAT_FULL;
        }
        return localDateTime.format (DateTimeFormatter.ofPattern (pattern));
    }

}
