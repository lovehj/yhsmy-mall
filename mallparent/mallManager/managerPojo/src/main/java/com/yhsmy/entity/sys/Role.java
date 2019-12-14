package com.yhsmy.entity.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/11/9 22:50
 **/
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = 665844089510536070L;

    private String roleId;

    @NotEmpty(message = "角色名不能为空!")
    @Size(min = 2, max = 40, message = "角色名称控制在2-40个汉字内!")
    private String roleName;

    @Size(min = 0, max = 150, message = "角色备注控制在150个字符内!")
    private String remark;

    /**
     * 记录操作状态 0=删除 1=正常
     */
    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
