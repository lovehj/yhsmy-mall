package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:50
 **/
@Data
public class LinkMessage extends BaseMessage {

    private static final long serialVersionUID = 2201378153261750123L;
    /**
     * 消息标题
     */
    private String Title;

    /**
     * 消息描述
     */
    private String Description;

    /**
     * 消息链接
     */
    private String Url;
}
