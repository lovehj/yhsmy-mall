package com.yhsmy.service.act.impl;

import com.yhsmy.service.act.ActAssigneeServiceI;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/14 12:18
 **/
@Service
@Transactional(readOnly = true)
public class ActAssigneeServiceImpl implements ActAssigneeServiceI {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ActivityImpl> getActivityList (String id) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery ()//
            .deploymentId (id).singleResult ();

        ProcessDefinitionEntity pdEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService)
            .getDeployedProcessDefinition (pd.getId ());

        InputStream in = repositoryService.getResourceAsStream (id, pd.getResourceName ());
        new BpmnXMLConverter ().convertToBpmnModel (new InputStreamSource (in), false, true);
        return getAllActivity(pdEntity.getActivities ());
    }

    private List<ActivityImpl> getAllActivity (List<ActivityImpl> activities) {
        List<ActivityImpl> list = new ArrayList<> (activities);
        for(ActivityImpl activity : activities) {
            List<ActivityImpl> childList = activity.getActivities ();
            if(!childList.isEmpty ()) {
                list.addAll (getAllActivity(childList));
            }
        }
        return list;
    }

    @Override
    public void deleteByNodeId (String nodeId) {

    }
}
