package com.yhsmy.entity.vo.sys;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

/**
 * @auth 李正义
 * @date 2019/12/26 17:43
 **/
public class Message extends com.yhsmy.entity.sys.Message {

    private static final long serialVersionUID = -7162694756103642349L;


    public String getCreateTimeStr(){
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }

    public String getFlagStr() {
        if(super.getFlag () == 1) {
            return "已读";
        }
        return "未读";
    }

    public String getPositionStr() {
        if(super.getPosition () == 1) {
            return "底部";
        }
        return "顶部";
    }

    public Message() { }

    public Message(String userId, String title, String content, String processUrl) {
        super(userId, title, content, processUrl);
    }

}
