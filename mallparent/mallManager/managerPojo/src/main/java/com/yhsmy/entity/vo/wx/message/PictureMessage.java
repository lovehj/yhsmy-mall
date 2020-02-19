package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:42
 **/
@Data
public class PictureMessage extends BaseMessage {

    private static final long serialVersionUID = -1423566106901041490L;
    /**
     * 图片链接
     */
    private String PicUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String MediaId;
}
