package com.yhsmy.service.wx;

import java.io.InputStream;

/**
 * @auth 李正义
 * @date 2020/2/13 12:01
 **/
public interface ProcessServiceI {

    /**
     * 处理微信推送过来的消息
     *
     * @param inputStream
     * @return
     */
    public String processRequest (InputStream inputStream);

    /**
     * 获取微信公众号的access_token
     * @return
     */
    public String getAccessToken();
}
