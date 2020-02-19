package com.yhsmy.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 微信事件类型枚举
 *
 * @auth 李正义
 * @date 2020/2/13 11:03
 **/
public enum WxEventEnum {

    /**
     * 订阅事件
     */
    SUBSCRIBE ("subscribe", "订阅事件"),

    /**
     * 取消订阅事件
     */
    UNSUBSCRIBE ("unsubscribe", "取消订阅事件"),

    /**
     * 关注后的扫码事件
     */
    SCAN ("SCAN", "关注后的扫码事件"),

    /**
     * 地理位置事件
     */
    LOCATION ("LOCATION", "地理位置事件"),

    /**
     * 自定义菜单事件
     */
    CLICK ("CLICK", "自定义菜单事件"),

    /**
     * 菜单跳转事件
     */
    VIEW ("VIEW", "菜单跳转事件");

    private String key;

    private String value;

    private WxEventEnum (String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValueByKey (String key) {
        if (StringUtils.isBlank (key)) {
            return "";
        }

        for (WxEventEnum event : values ()) {
            if (event.key.equals (key)) {
                return event.value;
            }
        }
        return "";

    }

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }
}
