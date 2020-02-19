package com.yhsmy.task;

import com.yhsmy.service.wx.ProcessServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 微信获取asscess_token，每一个半小时执行一次
 *
 * @auth 李正义
 * @date 2020/2/14 17:06
 **/
@Slf4j
public class WechatAccessTokenTask {

    @Autowired
    private ProcessServiceI processServiceI;

    /**
     * 实现逻辑：微信的access_token有效期为2小时
     * cron表达式格式：秒、分、时、日、月、周、年
     */
    @Async("executor")
    @Scheduled(fixedRate = 5400) //每1小时30分钟执行一次
    public void checkAccessToken () {
        try {
            processServiceI.getAccessToken ();
        } catch (Exception e) {
            log.error ("获取微信access_token失败!", e);
        }
    }


}
