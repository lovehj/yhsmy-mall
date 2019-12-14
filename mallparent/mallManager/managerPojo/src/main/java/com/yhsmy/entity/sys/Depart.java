package com.yhsmy.entity.sys;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/11/29 17:11
 **/
@Data
@NoArgsConstructor
public class Depart implements Serializable {
    private static final long serialVersionUID = 7416632213689687777L;

    public String id;

    @NotEmpty(message = "部门名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    public String name;

    public String pid;

    @Size(min = 0, max = 150, message = "部门备注控制在0-150个字符内！", groups = {AddGroup.class, UpdateGroup.class})
    public String remark;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
