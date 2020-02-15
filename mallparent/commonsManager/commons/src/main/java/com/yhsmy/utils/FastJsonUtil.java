package com.yhsmy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fastJson操作工具类
 *
 * @auth 李正义
 * @date 2019/12/8 17:57
 **/
public class FastJsonUtil {

    /**
     * list对象转换为JSONArray
     *
     * @param listData 被转换的数据
     * @return JSONArray
     */
    public static JSONArray listToJSONArray (List<?> listData) {
        if (listData == null) {
            listData = new ArrayList<> (1);
        }
        return JSONArray.parseArray (JSON.toJSONString (listData));
    }


    /**
     * list对象转换为JSONArray字符串
     *
     * @param listData 被转换的数据
     * @return JSON字符串
     */
    public static String listToJSONArrayString (List<?> listData) {
        return JSONArray.toJSONString (FastJsonUtil.listToJSONArray (listData));
    }

    /**
     * 把JSON格式字符串转对象
     *
     * @param jsonObj 被转换的JSON格式对象
     * @param tClass  转换对象
     * @return
     */
    public static <T> T parseJSONObjectToObject (Object jsonObj, Class<T> tClass) {
        return JSON.parseObject (JSON.toJSONString (jsonObj), tClass);
    }

    /**
     * javaBean转Map集合
     *
     * @param javaBean
     * @return
     */
    public static Map<String, Object> javaBeanToMap (Object javaBean) {
        return JSON.parseObject (JSON.toJSONString (javaBean), Map.class);
    }
}
