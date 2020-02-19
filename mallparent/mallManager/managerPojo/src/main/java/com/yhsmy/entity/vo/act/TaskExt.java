package com.yhsmy.entity.vo.act;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * activiti task的扩展
 *
 * @auth 李正义
 * @date 2019/12/24 21:59
 **/
@Data
public class TaskExt implements Serializable {
    private static final long serialVersionUID = -686714152824073929L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行ID
     */
    private String executionId;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 流程定义ID
     */
    private String procDefId;

    /**
     * 备注
     */
    private String description;

    /**
     * 当前节点KEY
     */
    private String taskDefKey;

    private String owner;

    /**
     * 办理人
     */
    private String assignee;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 表单KEY
     */
    private String formKey;

    /**
     * 申请人
     */
    private String auditName;

    /**
     * 申请类型KEY
     */
    private String auditCtypeKey;

    /**
     * 申请类型
     */
    private String auditCtypeName;

    /**
     * 申请内容
     */
    private String auditContent;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 办理时长
     */
    private String durationTime;

    /**
     * 流程启动的KEY
     */
    private String startKey;

    public TaskExt () {

    }

    public TaskExt (Task task) {
        this.taskId = task.getId ();
        this.taskName = task.getName ();
        this.executionId = task.getExecutionId ();
        this.procInstId = task.getProcessInstanceId ();
        this.procDefId = task.getProcessDefinitionId ();
        this.description = task.getDescription ();
        this.taskDefKey = task.getTaskDefinitionKey ();
        this.owner = task.getOwner ();
        this.assignee = task.getAssignee ();
        this.priority = task.getPriority ();
        this.createTime = task.getCreateTime ();
        this.formKey = task.getFormKey ();
        if(StringUtils.isNotEmpty (procDefId)) {
            this.startKey = procDefId.split (":")[0];
        }
    }

    public TaskExt (HistoricTaskInstance taskInstance) {
        this.taskName = taskInstance.getName ();
        this.executionId = taskInstance.getExecutionId ();
        this.procInstId = taskInstance.getProcessInstanceId ();
        this.procDefId = taskInstance.getProcessDefinitionId ();
        this.description = taskInstance.getDescription ();
        this.taskDefKey = taskInstance.getTaskDefinitionKey ();
        this.owner = taskInstance.getOwner ();
        this.assignee = taskInstance.getAssignee ();
        this.priority = taskInstance.getPriority ();
        this.createTime = taskInstance.getCreateTime ();
        this.formKey = taskInstance.getFormKey ();
        this.assignee = taskInstance.getAssignee ();
        this.startTime = DateTimeUtil.formatDate (taskInstance.getStartTime ());
        this.endTime = DateTimeUtil.formatDate (taskInstance.getEndTime ());
        this.durationTime = DateTimeUtil.durationForDays (taskInstance.getDurationInMillis ());
        if(StringUtils.isNotEmpty (taskInstance.getProcessDefinitionId ())) {
            this.startKey = taskInstance.getProcessDefinitionId ().split (":")[0];
        }
    }

    public String getCreateTimeStr () {
        return DateTimeUtil.formatDate (this.createTime);
    }


}
