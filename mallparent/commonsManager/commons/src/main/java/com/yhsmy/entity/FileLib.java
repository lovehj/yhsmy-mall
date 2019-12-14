package com.yhsmy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2019/12/1 16:58
 **/
@Data
public class FileLib implements Serializable {

    /**
     *  0=图片 1=附件
     */
    private int tableType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 请求的URL前缀
     */
    private String urlPrefix;

    public int getFileCtype(String ext) {
        boolean flag = ext.indexOf ("jpg") > -1 || ext.indexOf ("png") > -1
                || ext.indexOf ("jpeg") > -1 || ext.indexOf ("bmp") > -1
                || ext.indexOf ("gif")>-1;
        if(flag) {
            return 0;
        }
        return 1;
    }

}
