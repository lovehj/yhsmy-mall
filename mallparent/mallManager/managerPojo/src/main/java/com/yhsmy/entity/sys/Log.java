package com.yhsmy.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志表
 *
 * @auth 李正义
 * @date 2019/12/10 9:36
 **/
@Data
public class Log implements Serializable {
    private static final long serialVersionUID = -8016188540636780195L;

    private String logId;

    /**
     * 操作的用户
     */
    private String operator;

    /**
     * 操作类型包含 ADD/UPDATE/DELETE/SELECT/ATHOR
     */
    private String operaType;

    /**
     * 操作的IP
     */
    private String fromIp;

    /**
     * 操作的国家·省份/城市
     */
    private String fromArea;

    /**
     * 经度|纬度
     */
    private String fromLonLat;

    /**
     * 设备来源
     */
    private String fromDevice;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 操作备注
     */
    private String remark;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    private int state;

    private LocalDateTime createTime;

}
