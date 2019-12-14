package com.yhsmy.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/8 10:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGrid implements Serializable {

    private int code;

    private String msg;

    private long count;

    private long pageNum;

    private List<?> data;

    public DataGrid(long count, List<?> data) {
        this.count = count;
        this.data = data;
    }

    public DataGrid(long count, long pageNum, List<?> data) {
        this.count = count;
        this.pageNum = pageNum;
        this.data = data;
    }

    public static String jsonString(long count, List<?> data,
                                    Map<String, Map<String, Object>> map, String node) {
        JSONObject obj = new JSONObject ();
        if(data == null || data.size () <= 0) {
            return obj.toJSONString ();
        }
        JSONArray arr = JSONArray.parseArray (JSON.toJSONString (data));
        int arrSize = arr.size ();
        for(int i=0; i<arrSize; i++) {
            JSONObject jsonData = arr.getJSONObject (i);
            jsonData.putAll (map.get (jsonData.get (node)));
        }
        obj.put ("count", count);
        obj.put ("data", arr);
        obj.put ("code", 0);
        obj.put ("msg", "");
        return obj.toJSONString ();
    }


}
