package com.yhsmy.entity.vo.wx.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信事件基类
 *
 * @auth 李正义
 * @date 2020/2/13 11:12
 **/
@Data
public abstract class BaseEvent implements Serializable {

    private static final long serialVersionUID = 7439709390728505264L;
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
     * 消息类型，event
     */
    private String MsgType;

    /**
     * 事件类型
     */
    private String Event;
}
