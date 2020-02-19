package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * 回复音乐消息
 *
 * @auth 李正义
 * @date 2020/2/13 11:51
 **/
@Data
public class MusicMessage extends BaseReplyMessage {

    private static final long serialVersionUID = -3087467700016237405L;
    /**
     * 回复音乐消息
     */
    private Music Music;

    @Data
    class Music implements Serializable {

        /**
         * 音乐标题
         */
        private String Title;

        /**
         * 音乐描述
         */
        private String Description;

        /**
         * 音乐链接
         */
        private String MusicUrl;

        /**
         * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
         */
        private String HQMusicUrl;

        /**
         * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
         */
        private String ThumbMediaId;
    }
}
