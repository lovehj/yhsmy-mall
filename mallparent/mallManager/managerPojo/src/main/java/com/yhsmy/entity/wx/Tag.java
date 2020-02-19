package com.yhsmy.entity.wx;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 微信公众号设置用户标签
 *
 * @auth 李正义
 * @date 2020/2/15 14:11
 **/
@Data
public class Tag implements Serializable {

    private static final long serialVersionUID = 5905252748869940538L;

    private int id;

    @NotEmpty(message = "标签名不能为空!")
    @Size(min = 2, max = 30, message = "标签名控制在2-30个汉字内!")
    private String name;

    private int state;

    private String creator;

    private String createTime;

    private String modifyor;

    private String modifyTime;
}
