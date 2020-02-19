package com.yhsmy.enums;

import com.yhsmy.entity.Approve;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @auth 李正义
 * @date 2020/1/5 18:08
 **/
public enum ContentCateTypeEnum {

    /**
     * 轮播图
     */
    CAROUSEL (1, "轮播图"),

    /**
     * 顶部广告
     */
    TOP_ADVERT (2, "顶部广告"),

    /**
     * 侧边广告
     */
    LEFT_ADVERT (3, "侧边广告"),

    /**
     * 底部广告
     */
    BOTTOM_ADVERT (4, "底部广告"),

    /**
     * 页面中心广告
     */
    CENTER_ADVERT (5, "页面中心广告"),

    /**
     * 页面中心备选1
     */
    CENTER_STANDBY_FIRST (6, "页面中心备选1"),

    /**
     * 页面中心备选2
     */
    CENTER_STANDBY_SECONED (7, "页面中心备选2"),

    /**
     * 页面中心备选3
     */
    CENTER_STANDBY_THIRED (8, "页面中心备选3"),

    /**
     * 页面中心备选4
     */
    CENTER_STANDBY_FOURTH (9, "页面中心备选4"),

    /**
     * 其它位置
     */
    OTHER_POSITION (10, "其它位置");

    private int key;

    private String value;

    private ContentCateTypeEnum (int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey () {
        return key;
    }

    public void setKey (int key) {
        this.key = key;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public static String getValueByKey (int key) {
        for (ContentCateTypeEnum cate : values ()) {
            if (cate.getKey () == key) {
                return cate.getValue ();
            }
        }
        return "";
    }

    public static List<Map<String, String>> getList () {
        List<Map<String, String>> list = new ArrayList<> (10);
        Map<String, String> map = new TreeMap<> ();
        list.add (map);
        for (ContentCateTypeEnum cate : values ()) {
            map.put (String.valueOf (cate.getKey ()), cate.getValue ());
        }
        return list;
    }
}
