package com.yhsmy.entity.vo.wx.reply;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2020/2/13 11:42
 **/
@Data
public class ImageMessage extends BaseReplyMessage {

    private static final long serialVersionUID = -5022304484655816725L;

    /**
     * 回复图片
     */
    private List<Map<String,Object>> Image;

}

