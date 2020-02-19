package com.yhsmy.enums;

/**
 * @auth 李正义
 * @date 2019/12/14 13:28
 **/
public enum AssigneeTypeEnum {

    /**
     * 办理人
     */
    USER (0, "办理人"),

    /**
     * 候选人
     */
    USERS (1, "候选人"),

    /**
     * 组
     */
    GROUP(2, "组");

    private int key;

    private String value;

    private AssigneeTypeEnum (int key, String value) {
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
        for (AssigneeTypeEnum status : values ()) {
            if (status.getKey () == key) {
                return status.getValue ();
            }
        }
        return "";
    }

}
