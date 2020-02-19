package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;

import java.io.Serializable;

/**
 * 回复图文消息
 *
 * @auth 李正义
 * @date 2020/2/13 11:55
 **/
@Data
public class ArticlesMessage extends BaseReplyMessage {

    private static final long serialVersionUID = 2676739777913199500L;
    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private int ArticleCount;

    /**
     * 回复图文消息
     */
    private Articles Articles;

    @Data
    class Articles implements Serializable {

        private static final long serialVersionUID = 2127902628318012484L;

        private Item Item;
    }

    @Data
    class Item implements Serializable {

        private static final long serialVersionUID = 4918116861389286221L;
        /**
         * 图文消息标题
         */
        private String Title;

        /**
         * 图文消息描述
         */
        private String Description;

        /**
         * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
         */
        private String PicUrl;

        /**
         * 点击图文消息跳转链接
         */
        private String Url;
    }
}
