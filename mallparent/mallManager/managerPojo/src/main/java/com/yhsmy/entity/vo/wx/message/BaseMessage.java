package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2020/2/13 10:27
 **/
@Data
public abstract class BaseMessage implements Serializable {

    /**
     * 开发者微信号
     */
    private String ToUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String FromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private long CreateTime;

    /**
     * 消息类型
     */
    private String MsgType;

    /**
     * 消息id，64位整型
     */
    private String MsgId;

}
