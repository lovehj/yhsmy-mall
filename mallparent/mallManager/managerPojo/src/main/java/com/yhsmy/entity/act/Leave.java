package com.yhsmy.entity.act;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yhsmy.IConstant;
import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/12/20 17:01
 **/
@Data
public class Leave implements Serializable {
    private static final long serialVersionUID = 6210939331451958130L;

    private String leaveId;

    private String userId;

    @NotBlank(message = "请假天数不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String days;

    @NotBlank(message = "请假事由不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 120,message = "请假事由控制在120个字符内!")
    private String content;

    @NotNull(message = "请假开始时间不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @DateTimeFormat(pattern = IConstant.LOCALDATETIME_FORMAT)
    private LocalDateTime startDate;

    @NotNull(message = "请假结束时间不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @DateTimeFormat(pattern = IConstant.LOCALDATETIME_FORMAT)
    private LocalDateTime endDate;

    /**
     * 流程实例ID,数据来源于activiti启动流程后的ID
     */
    private String processInstanceId;

    private String remark;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
