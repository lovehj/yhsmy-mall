package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * 回复语音消息
 *
 * @auth 李正义
 * @date 2020/2/13 11:46
 **/
@Data
public class VoiceReplyMessage extends BaseReplyMessage {

    private static final long serialVersionUID = 3798510398333874692L;

    /**
     * 回复语音消息
     */
    private Voice Voice;

    @Data
    public static class Voice implements Serializable {

        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id
         */
        private String MediaId;

    }
}
