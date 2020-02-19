package com.yhsmy.entity.vo.wx.event;

import lombok.Data;
import org.apache.commons.math3.util.Precision;

/**
 * 地址位置事件
 *
 * @auth 李正义
 * @date 2020/2/13 11:22
 **/
@Data
public class LocationEvent extends BaseEvent {

    private static final long serialVersionUID = 3199403608152926938L;
    /**
     * 地理位置纬度
     */
    private String Latitude;

    /**
     * 地理位置经度
     */
    private String Longitude;

    /**
     * 地理位置精度
     */
    private String Precision;
}
