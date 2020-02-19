package com.yhsmy.act.listener;

import com.yhsmy.service.act.ActAssigneeServiceI;
import com.yhsmy.utils.SpringUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @auth 李正义
 * @date 2019/12/14 11:48
 * 流程监听器 动态注入节点办理人
 **/
public class ActNodeListener implements TaskListener {

    @Override
    public void notify (DelegateTask delegateTask) {
        String defKey = delegateTask.getTaskDefinitionKey ();
        ActAssigneeServiceI actAssigneeServiceI = SpringUtil.getBean (ActAssigneeServiceI.class);

    }
}
