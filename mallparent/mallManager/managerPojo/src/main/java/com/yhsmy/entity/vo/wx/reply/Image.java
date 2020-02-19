package com.yhsmy.entity.vo.wx.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2020/2/17 17:24
 **/
@Data
@AllArgsConstructor
public class Image implements Serializable {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String MediaId;
}
