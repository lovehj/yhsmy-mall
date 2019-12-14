package com.yhsmy.entity.vo.sys;

import com.yhsmy.utils.DateTimeUtil;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2019/12/10 9:41
 **/
public class Log extends com.yhsmy.entity.sys.Log {
    private static final long serialVersionUID = -2705860422875068903L;

    public String getCreateTimeStr(){
        return DateTimeUtil.localDateTimeToStr (getCreateTime ());
    }

}
