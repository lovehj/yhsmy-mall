package com.yhsmy.service.act.impl;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.task.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/25 22:55
 **/
@Service
public class JumpTaskCmdService implements Command<Comment> {

    protected String executionId;  //当前任务的executionID

    protected String activityId;  //跳转目标activityID

    public JumpTaskCmdService(){

    }

    public JumpTaskCmdService(String executionId, String activityId) {
        this.executionId = executionId;
        this.activityId = activityId;
    }

    @Override
    public Comment execute (CommandContext commandContext) {
        List<TaskEntity> taskEntityList = Context.getCommandContext().getTaskEntityManager().findTasksByExecutionId(executionId);
        for(TaskEntity taskEntity : taskEntityList) {
            Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, "jump", false);
        }
        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager().findExecutionById(executionId);
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
        ActivityImpl activity = processDefinition.findActivity(activityId);
        executionEntity.executeActivity(activity);
        return null;
    }
}
