package com.yhsmy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMMAT_FULL);
        return sdf.format (date);
    }

    public static String durationForDays(long millis) {
        if(millis <= 0) {
            return "0天0时0分0秒";
        }
        long days =  millis / DateUtils.MILLIS_PER_DAY;
        long hours = millis / DateUtils.MILLIS_PER_HOUR;
        if(hours > 24) {
            hours = millis % 24;
        }
        long minute = millis / DateUtils.MILLIS_PER_MINUTE;
        if(minute > 60) {
            minute = minute % 60;
        }
        long second = millis / DateUtils.MILLIS_PER_SECOND;
        if(second > 60) {
            second = second % 60;
        }
        return days+"天"+hours+"时"+minute+"分"+second+"秒";
    }

}
