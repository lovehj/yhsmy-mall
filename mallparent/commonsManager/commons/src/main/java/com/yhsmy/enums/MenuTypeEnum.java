package com.yhsmy.enums;

/**
 * 菜单类型枚举
 *
 * @auth 李正义
 * @date 2019/11/18 20:28
 **/
public enum MenuTypeEnum {

    /**
     * 目录
     */
    DIR (0, "目录"),

    /**
     * 菜单
     */
    MENU (1, "菜单"),

    /**
     * 操作
     */
    OPERA (2, "操作");

    private int key;

    private String value;

    private MenuTypeEnum (int key, String value) {
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
        for (MenuTypeEnum status : values ()) {
            if (status.getKey () == key) {
                return status.getValue ();
            }
        }
        return "";
    }

}
