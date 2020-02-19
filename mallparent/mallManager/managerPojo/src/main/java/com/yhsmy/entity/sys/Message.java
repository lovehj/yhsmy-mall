package com.yhsmy.entity.sys;

import com.yhsmy.enums.NormalEnum;
import com.yhsmy.utils.UUIDUtil;
import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息模块
 * @auth 李正义
 * @date 2019/12/26 17:38
 **/
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 7196303191675558484L;

    private String id;

    private String userId;

    /**
     * 0=顶部位置 1=底部位置
     */
    private int position;

    /**
     * 0=未读 1=已读
     */
    private int flag;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 100, message = "消息标题控制在100个字符内！")
    private String title;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 300, message = "消息内容控制在300个字符内！")
    private String content;

    /**
     * 处理地址
     */
    @NotBlank(message = "处理地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 200, message = "处理地址控制在200个字符内！")
    private String processUrl;

    private int state;

    private LocalDateTime createTime;

    public Message(){}

    public Message(String userId, String title, String content, String processUrl) {
        this.id = UUIDUtil.generateUUID ();
        this.userId = userId;
        this.position = 0;
        this.flag = 0;
        this.title = title;
        this.content = content;
        this.processUrl = processUrl;
        this.state = NormalEnum.NORMAL.getKey ();
        this.createTime = LocalDateTime.now ();
    }
}
