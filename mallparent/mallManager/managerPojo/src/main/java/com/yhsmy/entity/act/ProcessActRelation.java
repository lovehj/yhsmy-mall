package com.yhsmy.entity.act;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 保存页面创建流程图的关联关系
 *
 * @auth 李正义
 * @date 2019/12/20 15:01
 **/
@Data
public class ProcessActRelation implements Serializable {
    private static final long serialVersionUID = -4370906924551863204L;

    private String id;

    @NotBlank(message = "流程的KEY不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 40, message = "流程的KEY控制在40个字符内！")
    private String processKey;

    private String processName;

    @Size(min = 0, max = 30, message = "流程的备注控制在30个字符内！")
    private String description;

    private String modelId;

    private int state;

    private String creator;

    private LocalDateTime createTime;

}
