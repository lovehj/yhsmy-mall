package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

/**
 * 回复文本消息
 *
 * @auth 李正义
 * @date 2020/2/13 11:41
 **/
@Data
public class TextReplyMessage extends BaseReplyMessage {

    private static final long serialVersionUID = 2847948375142386285L;
    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    private String Content;
}
