package com.yhsmy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yhsmy.entity.Json;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    /**
     * 腾讯地址需要的KEY
     */
    @Value("${wx.tencentMapKey}")
    public String tencentMapKey;

    /**
     * 腾讯地图提供的webservice服务,带/线
     */
    @Value("${wx.tencentMapUrlPirfix}")
    private String tencentMapUrlPirfix;


    private String cgiBin ;

    @PostConstruct
    public void init() {
        cgiBin = wechatPrifix + wechatMiddlePart;
    }

    /**
     * 获取公众号accessToken的URL地址
     *
     * @return
     */
    public String getAccessToken() {
        return cgiBin + "token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
    }

    /**
     * 获取微信公众号的用户信息
     *
     * @param accessToken 微信公众号的调用凭据
     * @param openId      微信公众号用户的openId
     * @return
     */
    public String getUserInfo(String accessToken, String openId) {
        return cgiBin + "user/info?access_token=" + accessToken + "&openid=" + openId;
    }

    /**
     * 获取微信公众号自定义菜单的URL地址
     *
     * @param accessToken
     * @return
     */
    public String getCreateCustomMenu(String accessToken) {
        return cgiBin + "menu/create?access_token=" + accessToken;
    }

    /**
     * 解析access_token
     *
     * @param accessTokenJsonVal
     * @return
     */
    public String parseAccessToken(String accessTokenJsonVal) {
        if (StringUtils.isEmpty(accessTokenJsonVal)) {
            return null;
        }
        try {
            JSONObject obj = JSON.parseObject(accessTokenJsonVal);
            String accessToken = obj.getString("access_token");
            if (StringUtils.isNotEmpty(accessToken)) {
                return accessToken;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 青云客聊天机器人
     *
     * @return
     */
    public String getRobotUrl() {
        return robotUrl;
    }

    /**
     * 纬度和经度获取详细地址位置
     *
     * @param lat 纬度
     * @param lng 经度
     * @return url
     */
    public String getTencentMapByLatAndLng(float lat, float lng) {
        return tencentMapUrlPirfix + "geocoder/v1/?location=" + lat + "," + lng + "&get_poi=1&key=" + tencentMapKey;
    }

    /**
     * 根据IPV4定位
     *
     * @param ipv4
     * @return
     */
    public String getLocationByIpv4(String ipv4) {
        return tencentMapUrlPirfix + "location/v1/ip?ip=" + ipv4 + "&key=" + tencentMapKey;
    }


}
