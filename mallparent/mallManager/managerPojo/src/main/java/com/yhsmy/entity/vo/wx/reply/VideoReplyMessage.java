package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * 回复视频消息
 *
 * @auth 李正义
 * @date 2020/2/13 11:48
 **/
@Data
public class VideoReplyMessage extends BaseReplyMessage {

    private static final long serialVersionUID = -3189016800968718358L;

    /**
     * 回复视频消息
     */
    private Video Video;

    @Data
    class Video implements Serializable {
        private static final long serialVersionUID = 4892397971639133381L;

        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id
         */
        private String MediaId;

        /**
         * 视频消息的标题
         */
        private String Title;

        /**
         * 视频消息的描述
         */
        private String Description;
    }
}
