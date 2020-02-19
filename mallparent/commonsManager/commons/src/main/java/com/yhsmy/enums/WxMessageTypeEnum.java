package com.yhsmy.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 微信消息类型枚举
 *
 * @auth 李正义
 * @date 2020/2/13 10:53
 **/
public enum WxMessageTypeEnum {

    /**
     * 文本消息
     */
    TEXT ("text", "文本"),

    /**
     * 图片消息
     */
    IMAGE ("image", "图片"),

    /**
     * 语音消息
     */
    VOICE ("voice", "语音"),

    /**
     * 视频消息
     */
    VIDEO ("video", "视频"),

    /**
     * 小视频消息
     */
    SHORTVIDEO ("shortvideo", "小视频"),

    /**
     * 地理位置消息
     */
    LOCATION ("location", "地理位置"),

    /**
     * 链接消息
     */
    LINK ("link", "链接");

    private String key;

    private String value;

    private WxMessageTypeEnum (String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValueByKey (String key) {
        if (StringUtils.isBlank (key)) {
            return "";
        }

        for (WxMessageTypeEnum typeEnum : values ()) {
            if (typeEnum.key.equals (key)) {
                return typeEnum.value;
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
