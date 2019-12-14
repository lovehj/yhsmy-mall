package com.yhsmy.entity.sys;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/11/18 17:51
 **/
@Data
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = -2349925071422437455L;

    private String id;

    @NotEmpty(message = "菜单名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    @Size(min = 0, max = 100, message = "菜单地址控制在0-100个字符内！")
    private String url;

    @Size(min = 0, max = 50, message = "图标控制在0-50个字符内！")
    private String icon;

    @Size(min = 0, max = 100, message = "菜单权限控制在0-100个字符内！")
    private String permission;

    @Min (value = 0, message = "菜单的最小类型为目录", groups = {AddGroup.class})
    @Max (value = 2, message = "菜单的最大类型为按钮", groups = {AddGroup.class})
    private int ctype;

    private int orderNum;

    private String pid;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;


}
