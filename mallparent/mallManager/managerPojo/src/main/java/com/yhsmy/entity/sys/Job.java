package com.yhsmy.entity.sys;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2020/2/15 14:23
 **/
@Data
public class Job implements Serializable {
    private static final long serialVersionUID = 5141027906291631040L;

    private String id;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 100, message = "任务名称控制在100个字符内！")
    private String name;

    /**
     * 任务执行的类名
     */
    @NotBlank(message = "任务执行的类名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 10, max = 255, message = "任务执行的类名控制在10-255个字符内！")
    private String classPath;

    /**
     * 任务描述
     */
    @Size(min = 1, max = 150, message = "任务执行的类名控制在1-150个字符内！")
    private String remark;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
