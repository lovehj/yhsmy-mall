package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信公众号被动回复消息实体基类
 *
 * @auth 李正义
 * @date 2020/2/13 11:39
 **/
@Data
public abstract class BaseReplyMessage implements Serializable {

    private static final long serialVersionUID = 7386243794821185906L;
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String ToUserName;

    /**
     * 开发者微信号
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
}
