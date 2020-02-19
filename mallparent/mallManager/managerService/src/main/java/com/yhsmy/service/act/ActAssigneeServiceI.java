package com.yhsmy.service.act;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/14 12:18
 **/
public interface ActAssigneeServiceI {

    public List<ActivityImpl> getActivityList(String id);


    public void deleteByNodeId(String nodeId);
}
