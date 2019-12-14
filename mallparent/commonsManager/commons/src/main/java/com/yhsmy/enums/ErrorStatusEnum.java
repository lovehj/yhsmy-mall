package com.yhsmy.enums;

/**
 * @auth 李正义
 * @date 2019/11/8 10:02
 **/
public enum ErrorStatusEnum {

    PAGE_NOT_FOUND(404,"页面未找到"),

    FORBIDDEN(403, "页面未授权"),

    INTERNAL(500, "内部错误");

    private int key;

    private String value;

    private ErrorStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey () {
        return key;
    }

    public void setKey (int key) {
        this.key = key;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    /**
     * 根据key获取value值
     *
     * @param key
     * @return
     */
    public static String getValueByKey (int key) {
        for (ErrorStatusEnum status : values ()) {
            if (status.getKey () == key) {
                return status.getValue ();
            }
        }
        return "";
    }
}
