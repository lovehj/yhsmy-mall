package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:46
 **/
@Data
public class VideoMessage extends BaseMessage {

    private static final long serialVersionUID = 8064133867181282552L;
    /**
     * 视频消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String MediaId;

    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String ThumbMediaId;
}
