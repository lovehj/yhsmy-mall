package com.yhsmy.entity.vo.file;

import lombok.Data;

/**
 * @auth 李正义
 * @date 2019/12/1 17:38
 **/
@Data
public class FileLib extends com.yhsmy.entity.file.FileLib {

    /**
     * 请求的URL前缀
     */
    private String urlPrefix;


}
