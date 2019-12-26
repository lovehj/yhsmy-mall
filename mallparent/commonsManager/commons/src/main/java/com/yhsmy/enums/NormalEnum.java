package com.yhsmy.enums;

/**
 * 记录操作状态枚举类
 *
 * @auth 李正义
 * @date 2019/11/8 9:23
 **/
public enum NormalEnum {

    /**
     * 删除
     */
    DELETE (0, "删除"),

    /**
     * 正常
     */
    NORMAL (1, "正常"),

    /**
     * 冻结
     */
    FREEZE (2, "冻结"),

    /**
     * 商品下架
     */
    DOWN(3, "下架"),

    /**
     * 审批中
     */
    AUDITING(10, "审批中"),

    /**
     * 审批通过
     */
    AUDIT_PASS(11, "审批通过"),

    /**
     * 审批退回
     */
    AUDIT_BACK(12, "审批退回");

    private int key;

    private String value;

    private NormalEnum (int key, String value) {
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
        for (NormalEnum status : values ()) {
            if (status.getKey () == key) {
                return status.getValue ();
            }
        }
        return "";
    }
}
