package com.yhsmy.util;

import com.yhsmy.entity.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 管理微信公众号的URL地址
 *
 * @auth 李正义
 * @date 2020/2/15 11:19
 **/
@Component
public class WechatUrlUtil {

    @Value("${wx.appId}")
    public String appId;

    @Value("${wx.appSecret}")
    public String appSecret;

    /**
     * 微信公众号的token
     */
    @Value("${wx.token}")
    public String token;

    /**
     * 微信公众号URL的前缀部分
     */
    @Value("${wx.wechatPrifix}")
    private String wechatPrifix;

    /**
     * 微信公众号URL的中间部分
     */
    @Value("${wx.wechatMiddlePart}")
    private String wechatMiddlePart;

    /**
     * 聊天机器人地址
     */
    @Value("${wx.robot}")
    private String robotUrl;



    private String cgiBin = wechatPrifix + wechatMiddlePart;

    /**
     * 获取公众号accessToken的URL地址
     *
     * @return
     */
    public String getAccessToken () {
        return cgiBin + "token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
    }

    /**
     * 获取微信公众号的用户信息
     *
     * @param accessToken 微信公众号的调用凭据
     * @param openId      微信公众号用户的openId
     * @return
     */
    public String getUserInfo (String accessToken, String openId) {
        return cgiBin + "user/info?access_token=" + accessToken + "&openid=" + openId;
    }

    /**
     * 青云客聊天机器人
     * @return
     */
    public String getRobotUrl() {return robotUrl; }


}
