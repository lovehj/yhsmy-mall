package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:44
 **/
@Data
public class VoiceMessage extends BaseMessage {

    private static final long serialVersionUID = -4793721136348200153L;
    /**
     * 语音消息媒体id，可以调用获取临时素材接口拉取该媒体
     */
    private String MediaID;

    /**
     * 语音格式：amr
     */
    private String Format;

    /**
     * 语音识别结果，UTF8编码
     */
    private String Recognition;
}
