package com.yhsmy.entity;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2019/11/27 18:12
 **/
@Data
public class QueryParams {

    /**
     * 字符串查询参数，queryBy=查询字符串分类，queryText=字符串查询的值
     */
    private int queryBy;
    private String queryText;

    /**
     * 按日期查询参数，queryDate=查询的日期分类， startDate=开始时间，endDate=结束时间
     */
    private int queryDate;
    private String startDate;
    private String endDate;

    /**
     * 分页参数，page起始位置，limit结束位置
     */
    private String page;
    private String limit;

    public int getPageNo () {
        try {
            return Integer.parseInt (page);
        } catch (Exception e) {
            return 1;
        }
    }

    public int getPageSize () {
        try {
            return Integer.parseInt (limit);
        } catch (Exception e) {
            return 10;
        }
    }


}
