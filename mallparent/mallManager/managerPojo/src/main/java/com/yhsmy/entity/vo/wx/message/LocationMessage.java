package com.yhsmy.entity.vo.wx.message;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/2/13 10:47
 **/
@Data
public class LocationMessage extends BaseMessage {

    private static final long serialVersionUID = 5500165627006105819L;
    /**
     * 地理位置维度
     */
    private String Location_X;

    /**
     * 地理位置经度
     */
    private String Location_Y;

    /**
     * 地图缩放大小
     */
    private int Scale;

    /**
     * 地理位置信息
     */
    private String Label;
}
