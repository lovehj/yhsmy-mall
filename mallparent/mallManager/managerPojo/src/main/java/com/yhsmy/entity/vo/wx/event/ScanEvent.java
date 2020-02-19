package com.yhsmy.entity.vo.wx.event;

import lombok.Data;

/**
 * 微信扫码事件：
 * 1、还未关注公众号，则用户关注公众号，关注后将场景值推送给开发者
 * 2、已关注公众号，则推送扫码事件给开发者
 *
 * @auth 李正义
 * @date 2020/2/13 11:18
 **/
@Data
public class ScanEvent extends BaseEvent {

    private static final long serialVersionUID = 4052169986594048516L;

    /**
     * 未关注：事件KEY值，qrscene_为前缀，后面为二维码的参数值
     * 已关注：事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
     */
    private String EventKey;

    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String Ticket;
}
