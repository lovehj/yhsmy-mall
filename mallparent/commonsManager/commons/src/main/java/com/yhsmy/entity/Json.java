package com.yhsmy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2019/11/6 21:55
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Json implements Serializable {

    private static final long serialVersionUID = 5063971317557161574L;

    public static int SUC_CODE = 200;
    public static int FAIL_CODE = 0;
    public static String SUC_MSG = "操作成功！";
    public static String FAIL_MSG = "操作失败!";

    /**
     * 操作返回状态码 0=失败 200=成功
     */
    private int status;

    /**
     * 操作返回提示信息
     */
    private String msg;

    /**
     * 操作返回数据
     */
    private Object obj;


    public static Json ok(){
        return new Json (Json.SUC_CODE, Json.SUC_MSG, null);
    }

    public static Json ok(String msg) {
        return new Json(Json.SUC_CODE, msg, null);
    }

    public static Json ok(Object obj) {
        return new Json (Json.SUC_CODE, Json.SUC_MSG, obj);
    }

    public static Json ok(String msg, Object obj) {
        return new Json (Json.SUC_CODE, msg, obj);
    }

    public static Json fail() {
        return new Json(Json.FAIL_CODE, Json.FAIL_MSG, null);
    }

    public static Json fail(String msg) {
        return new Json(Json.FAIL_CODE, msg, null);
    }

    public static Json fail(Object obj) {
        return new Json(Json.FAIL_CODE, Json.FAIL_MSG, obj);
    }

    public static Json fail(String msg, Object obj) {
        return new Json(Json.FAIL_CODE, msg, obj);
    }

    public static Json warp(int status, String msg) {
        return new Json(status, msg, null);
    }

    public static Json warp(int status, String msg, Object obj) {
        return new Json(status, msg, obj);
    }

}
