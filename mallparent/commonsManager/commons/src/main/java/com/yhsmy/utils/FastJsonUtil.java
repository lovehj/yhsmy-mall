package com.yhsmy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * fastJson操作工具类
 *
 * @auth 李正义
 * @date 2019/12/8 17:57
 **/
public class FastJsonUtil {

    /**
     * list对象转换为JSONArray
     * @param listData 被转换的数据
     * @return JSONArray
     */
    public static JSONArray listToJSONArray(List<?> listData) {
        if(listData == null) {
            listData = new ArrayList<> (1);
        }
        return JSONArray.parseArray (JSON.toJSONString (listData));
    }


    /**
     * list对象转换为JSONArray字符串
     * @param listData 被转换的数据
     * @return JSON字符串
     */
    public static String listToJSONArrayString(List<?> listData) {
        return JSONArray.toJSONString (FastJsonUtil.listToJSONArray (listData));
    }

}
