package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:40
 **/
@Data
public class TextMessage extends BaseMessage {
    private static final long serialVersionUID = 7987941517534331247L;

    /**
     * 文本消息内容
     */
    private String Content;
}
