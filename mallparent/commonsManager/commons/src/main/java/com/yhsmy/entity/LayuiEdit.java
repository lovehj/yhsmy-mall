package com.yhsmy.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2020/1/13 12:17
 **/
public class LayuiEdit implements Serializable {
    private static final long serialVersionUID = 5552021248096220313L;

    public static final int SUC_CODE = 0;
    public static final int FAIL_CODE = 1;
    public static final String SUC_MSG = "上传成功！";
    public static final String FAIL_MSG = "上传失败!";

    /**
     * 0表示成功，其它失败
     */
    private int code;

    // 提示信息
    private String msg;

    private LayuiEditData data;

    private LayuiEdit (int code, String msg, LayuiEditData data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 上传成功后的文件路径
     *
     * @param filePath 文件路径
     * @return
     */
    public static LayuiEdit ok (String filePath) {
        return new LayuiEdit (SUC_CODE, SUC_MSG, new LayuiEditData (filePath));
    }

    /**
     * 上传成功后的文件路径及文件标题
     *
     * @param filePath 文件路径
     * @param title    文件标题/文件名
     * @return
     */
    public static LayuiEdit ok (String filePath, String title) {
        return new LayuiEdit (SUC_CODE, SUC_MSG, new LayuiEditData (filePath, title));
    }

    public static LayuiEdit fail () {
        return new LayuiEdit (FAIL_CODE, FAIL_MSG, null);
    }

    public static LayuiEdit fail (String msg) {
        return new LayuiEdit (FAIL_CODE, StringUtils.isNotBlank (msg) ? msg : FAIL_MSG, null);
    }

    public int getCode () {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public LayuiEditData getData () {
        return data;
    }

    public void setData (LayuiEditData data) {
        this.data = data;
    }

    private static class LayuiEditData implements Serializable {
        private static final long serialVersionUID = 6461275351622029805L;
        /**
         * 图片路径
         */
        private String src;

        /**
         * 图片名称
         */
        private String title;

        public LayuiEditData () {
        }

        public LayuiEditData (String src) {
            this.src = src;
        }

        public LayuiEditData (String src, String title) {
            this.src = src;
            this.title = title;
        }

        public String getSrc () {
            return src;
        }

        public void setSrc (String src) {
            this.src = src;
        }

        public String getTitle () {
            return title;
        }

        public void setTitle (String title) {
            this.title = title;
        }
    }

}
