package com.yhsmy.enums;

/**rabbitMq发送消息时指定的枚举类
 * @auth 李正义
 * @date 2020/1/3 21:10
 **/
public enum RabbitMqOperaTypeEnum {

    /**
     * 邮件
     */
    EMAIL(1, "邮件"),

    /**
     * 短信
     */
    SMS(2, "短信"),

    /**
     * 邮件和短信
     */
    EMAILANDSMS(3, "邮件和短信");

    private int key;

    private String value;

    private RabbitMqOperaTypeEnum (int key, String value) {
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
        for (RabbitMqOperaTypeEnum status : values ()) {
            if (status.getKey () == key) {
                return status.getValue ();
            }
        }
        return "";
    }


}
