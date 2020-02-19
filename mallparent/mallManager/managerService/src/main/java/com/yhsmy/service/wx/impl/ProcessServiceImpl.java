package com.yhsmy.service.wx.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yhsmy.IConstant;
import com.yhsmy.entity.vo.wx.message.PictureMessage;
import com.yhsmy.entity.vo.wx.message.TextMessage;
import com.yhsmy.entity.vo.wx.reply.Image;
import com.yhsmy.entity.vo.wx.reply.ImageMessage;
import com.yhsmy.entity.vo.wx.reply.TextReplyMessage;
import com.yhsmy.enums.WxEventEnum;
import com.yhsmy.enums.WxMessageTypeEnum;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.service.wx.ProcessServiceI;
import com.yhsmy.util.WechatUrlUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.utils.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.time.Duration;
import java.util.*;

/**
 * 处理微信发送过来的XML格式数据
 *
 * @auth 李正义
 * @date 2020/2/13 12:03
 **/
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessServiceI {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatUrlUtil wechatUrlUtil;

    @Override
    public String processRequest (InputStream inputStream) {
        try {
            Map<String, String> requestMsgMap = WechatUtil.parseXml (inputStream);
            // 获取微信推送过来的消息类型
            String msgType = requestMsgMap.get (IConstant.WECHAT_MSGTYPE), eventType = requestMsgMap.get (IConstant.WECHAT_EVENT);
            return this.getResponse (requestMsgMap, msgType, eventType);
        } catch (Exception e) {
            log.error ("不能处理微信推送的消息!", e);
        }
        return "";
    }

    @Override
    public String getAccessToken () {
        String error = "获取微信公众号的access_token失败！";
        String accessToken = restTemplate.getForObject (wechatUrlUtil.getAccessToken (), String.class);
        if (StringUtils.isEmpty (accessToken)) {
            throw new ServiceException (error);
        }
        JSONObject json = JSON.parseObject (accessToken);
        String token = json.getString ("access_token");
        long expire = json.getLong ("expires_in");
        if (StringUtils.isEmpty (token) || expire <= 0) {
            throw new ServiceException (error);
        }
        redisTemplate.opsForValue ().set (IConstant.WECHAT_ACCESS_TOKEN, token, Duration.ofSeconds (expire));
        return token;
    }

    /**
     * @param requestMsgMap
     * @param msgType
     * @param eventType
     * @return
     */
    private String getResponse (Map<String, String> requestMsgMap, String msgType, String eventType) {
        if (StringUtils.isNotBlank (msgType) && !"event".equals (msgType)) {
            // 判断消息类型
            if (msgType.equals (WxMessageTypeEnum.TEXT.getKey ())) {
                // 文本消息
                TextMessage textMessage = FastJsonUtil.parseJSONObjectToObject (requestMsgMap, TextMessage.class);
                String result = this.replyTextMessage (textMessage.getFromUserName (), textMessage.getToUserName (), getRobotResult (textMessage.getContent ()));
                System.out.println (result);
                return result;
            } else if (msgType.equals (WxMessageTypeEnum.IMAGE.getKey ())) {
                // 图片消息
                PictureMessage picture = FastJsonUtil.parseJSONObjectToObject (requestMsgMap, PictureMessage.class);
                String result = this.replyPicMessage (picture.getFromUserName (), picture.getToUserName (), picture.getMediaId ());
                System.out.println (result);
                return result;
            } else if (msgType.equals (WxMessageTypeEnum.VOICE.getKey ())) {
                //语音消息

            } else if (msgType.equals (WxMessageTypeEnum.VIDEO.getKey ())) {
                //视频消息

            } else if (msgType.equals (WxMessageTypeEnum.SHORTVIDEO.getKey ())) {
                // 小视频消息

            } else if (msgType.equals (WxMessageTypeEnum.LOCATION.getKey ())) {
                // 地理位置消息

            } else if (msgType.equals (WxMessageTypeEnum.LINK.getKey ())) {
                // 链接消息

            }
        } else if ("event".equals (msgType) && StringUtils.isNotBlank (eventType)) {
            // 推送的事件类型
            if (eventType.equals (WxEventEnum.SUBSCRIBE.getKey ())) {
                // 订阅事件
                TextMessage textMessage = FastJsonUtil.parseJSONObjectToObject (requestMsgMap, TextMessage.class);
                String result = this.replyTextMessage (textMessage.getFromUserName (), textMessage.getToUserName (), "欢迎关注永红山盟院公众号！\n本公众号支持天气、翻译、藏头诗、笑话、歌词、手机号码归属、人工智能聊天等功能。\n使用方式：在公众号中回复关键字即可。");
                System.out.println (result);
                return result;
            } else if (eventType.equals (WxEventEnum.UNSUBSCRIBE.getKey ())) {
                // 取消订阅事件
            } else if (eventType.equals (WxEventEnum.SCAN.getKey ())) {
                // 扫码事件
            } else if (eventType.equals (WxEventEnum.LOCATION.getKey ())) {
                // 地址位置事件
            } else if (eventType.equals (WxEventEnum.CLICK.getKey ())) {
                // 菜单点击事件
            } else if (eventType.equals (WxEventEnum.VIEW.getKey ())) {
                // 菜单跳转事件
            }
        }
        return "";
    }


    private String getRobotResult (String searachWord) {
        String robotUrl = wechatUrlUtil.getRobotUrl () + searachWord, errorMsg = "这个句子我还在努力学习中，换一个简单的试试吧!";
        String result = restTemplate.getForObject (robotUrl, String.class);
        try {
            JSONObject parse = JSON.parseObject (result);
            if (parse.getInteger ("result") == 0) {
                return parse.getString ("content").replace ("{br}", "\n");
            }
        } catch (Exception e) {
            log.error ("解析青云客数据出错!", e);
        }
        return errorMsg;
    }

    /**
     * 回复文本消息
     *
     * @param toUserName 回复给那个微信号
     * @param fromUserName 微信公众号
     * @param textMessage 回复的文本信息
     * @return
     */
    private String replyTextMessage (String toUserName, String fromUserName, String textMessage) {
        TextReplyMessage _textMessage = new TextReplyMessage ();
        _textMessage.setToUserName (toUserName);
        _textMessage.setFromUserName (fromUserName);
        _textMessage.setContent (textMessage);
        _textMessage.setCreateTime (new Date ().getTime ());
        _textMessage.setMsgType (WxMessageTypeEnum.TEXT.getKey ());
        return WechatUtil.mapToXml (FastJsonUtil.javaBeanToMap (_textMessage));
    }

    private String replyPicMessage(String toUserName, String fromUserName, String mediaId) {
        List<Map<String,Object>> imageList = new ArrayList<> (1);
        Map<String,Object> mediaIdMap = new HashMap<> (1);
        mediaIdMap.put ("MediaId", mediaId);
        imageList.add (mediaIdMap);
        Map<String, Object> imageMap = setReplyMap (fromUserName, toUserName, WxMessageTypeEnum.IMAGE.getKey ());
        imageMap.put ("Image", imageList);
        return WechatUtil.mapToXml (imageMap);
    }

    private Map<String, Object> setReplyMap(String toUser, String fromUser, String replytType) {
        Map<String, Object> resultMap = new HashMap<> (1);
        resultMap.put ("FromUserName", toUser);
        resultMap.put ("ToUserName", fromUser);
        resultMap.put ("CreateTime", new Date ().getTime ());
        resultMap.put ("MsgType", replytType);
        return resultMap;
    }

}
