package com.yhsmy.web.activiti;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.ProcessDefinitionExt;
import com.yhsmy.service.act.ActAssigneeServiceI;
import com.yhsmy.service.act.ProcessActRelationServiceI;
import com.yhsmy.utils.Base64Utils;
import com.yhsmy.utils.UUIDUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.HMProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/14 12:17
 **/
@Controller
@Scope("request")
@RequestMapping("/act")
public class ActivitiController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private ActAssigneeServiceI actAssigneeServiceI;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    private ProcessEngineFactoryBean processEngineFactoryBean;

    @Autowired
    private ProcessActRelationServiceI processActRelationServiceI;

    @RequiresPermissions("act:processList")
    @ApiOperation(value = "流程部署页面")
    @GetMapping("processList")
    public String processList () {
        return "activiti/processList";
    }

    @RequiresPermissions("act:processList")
    @ApiOperation(value = "流程部署数据列表")
    @GetMapping("processListData")
    @ResponseBody
    public DataGrid processListData (Model model, ProcessDefinition definition, QueryParams queryParams) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery ();
        if (definition != null) {
            if (StringUtils.isNotBlank (definition.getDeploymentId ())) {
                definitionQuery.deploymentId (definition.getDeploymentId ());
            }

            if (StringUtils.isNotBlank (definition.getName ())) {
                definitionQuery.processDefinitionNameLike ("%" + definition.getName () + "%");
            }
        }

        List<ProcessDefinition> definitionList = definitionQuery.listPage (queryParams.getStartRow (), queryParams.getPageSize ());
        long count = repositoryService.createDeploymentQuery ().count ();
        List<ProcessDefinitionExt> list = new ArrayList<> (definitionList.size ());
        for (ProcessDefinition df : definitionList) {
            ProcessDefinitionExt ext = new ProcessDefinitionExt ();
            BeanUtils.copyProperties (df, ext);
            list.add (ext);
        }
        return new DataGrid (count, list);
    }

    @ApiOperation("流程部署删除")
    @RequiresPermissions("act:processListDelete")
    @DeleteMapping("processListDelete")
    @ResponseBody
    public Json processListDelete (String id) {
        try {
//            List<ActivityImpl> activityList = actAssigneeServiceI.getActivityList(id);
//            for(ActivityImpl activity : activityList) {
//                String nodeId = activity.getId ();
//                if(StringUtils.isEmpty(nodeId) || "start".equals(nodeId) || "end".equals(nodeId)) {
//                    continue;
//                }
//                actAssigneeServiceI.deleteByNodeId(nodeId);
//            }
            repositoryService.deleteDeployment (id, true);
        } catch (Exception e) {
            return Json.fail ();
        }
        return Json.ok ();
    }


    @RequiresPermissions("act:modelList")
    @ApiOperation(value = "流程列表页面")
    @GetMapping("modelList")
    public String modelList () {
        return "activiti/modelList";
    }

    @RequiresPermissions("act:modelList")
    @ApiOperation(value = "流程列表数据列表")
    @GetMapping("modelListData")
    @ResponseBody
    public DataGrid modelListData (QueryParams queryParams) {
        return processActRelationServiceI.getProcessActRelationList (queryParams);
    }

    @RequiresPermissions("act:modelForm")
    @ApiOperation(value = "流程表单页面")
    @GetMapping("modelForm")
    public String modelForm (String id, String processActId) throws Exception {
        if (StringUtils.isBlank (id)) {
            id = processActRelationServiceI.getModelForm (processActId);

        }
        return "redirect:/static/modeler.html?modelId=" + id;
    }

    @SysLog(content = "流程发布", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("act:deployProcess")
    @ApiOperation(value = "流程发布")
    @PostMapping("deployProcess")
    @ResponseBody
    public Json deployProcess (String id) {
        return processActRelationServiceI.deployProcess (id);
    }

    @SysLog(content = "流程模型删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @RequiresPermissions("act:deleteModel")
    @ApiOperation(value = "流程模型删除")
    @DeleteMapping("deleteModel")
    @ResponseBody
    public Json deleteModel (String id) {
        if (StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return processActRelationServiceI.deleteProcessActRelation (id);
    }

    @RequiresPermissions("act:syncdata")
    @ApiOperation(value = "同步用户数据到activiti的表中")
    @PostMapping("syncdata")
    @ResponseBody
    public Json syncdata () {
        return processActRelationServiceI.syncData ();
    }

    /**
     * 查看流程图
     *
     * @param model
     * @param processInstanceId
     * @return
     */
    @ApiOperation(value = "查看流程图")
    @GetMapping("/viewProcessImg/{processInstanceId}")
    public String viewProcessImg (Model model, @PathVariable String processInstanceId) {
        model.addAttribute ("processInstanceId", processInstanceId);
        return "activiti/viewProcessImg";
    }

    @ApiOperation(value = "获取JSON格式的流程图")
    @GetMapping("getViewProcessImg")
    @ResponseBody
    public String getViewProcessImg (String processInstanceId, HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject ();
        JSONArray array = new JSONArray ();
        InputStream imageCurrentNodeStream = generateStream (processInstanceId, request, response, true);
        if (imageCurrentNodeStream != null) {
            String imageCurrentNode = Base64Utils.io2Base64 (imageCurrentNodeStream);
            if (StringUtils.isNotBlank (imageCurrentNode)) {
                array.add (imageCurrentNode);
            }
        }

        InputStream imageNoCurrentNodeSteam = generateStream (processInstanceId, request, response, false);
        if (imageNoCurrentNodeSteam != null) {
            String imageNoCurrentNode = Base64Utils.io2Base64 (imageNoCurrentNodeSteam);
            if (StringUtils.isNotBlank (imageNoCurrentNode)) {
                array.add (imageNoCurrentNode);
            }

        }

        result.put ("id", UUIDUtil.generateUUID ());
        result.put ("images", array);
        return result.toJSONString ();
    }

    private InputStream generateStream (String processInstanceId, HttpServletRequest request, HttpServletResponse response, boolean needCurrentNode) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ()//
                .processInstanceId (processInstanceId).singleResult ();

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery ().processInstanceId (processInstanceId).singleResult ();

        String processDefinitionId = null;

        List<String> executedActivityIdList = new ArrayList<> (1);
        List<String> currentActivityIdList = new ArrayList<> (1);
        List<HistoricActivityInstance> historicActivityInstanceList = new ArrayList<> (1);

        if (processInstance != null) {
            processDefinitionId = processInstance.getProcessDefinitionId ();
            if (needCurrentNode) {
                currentActivityIdList = runtimeService.getActiveActivityIds (processInstance.getId ());
            }
        }

        if (historicProcessInstance != null) {
            processDefinitionId = historicProcessInstance.getProcessDefinitionId ();
            historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery () //
                    .processInstanceId (processInstanceId) //
                    .orderByHistoricActivityInstanceId ().asc ().list ();

            for (HistoricActivityInstance history : historicActivityInstanceList) {
                executedActivityIdList.add (history.getActivityId ());
            }
        }

        if (StringUtils.isBlank (processDefinitionId) || executedActivityIdList.isEmpty ()) {
            return null;
        }

        //高亮线路id集合
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition (processDefinitionId);
        List<String> highLightedFlows = getHighLightedFlows (definitionEntity, historicActivityInstanceList);

        BpmnModel bpmnModel = repositoryService.getBpmnModel (processDefinitionId);
        processEngineConfiguration = processEngineFactoryBean.getProcessEngineConfiguration ();
        Context.setProcessEngineConfiguration ((ProcessEngineConfigurationImpl) processEngineConfiguration);
        HMProcessDiagramGenerator diagramGenerator = (HMProcessDiagramGenerator) processEngineConfiguration.getProcessDiagramGenerator ();
        ProcessEngineConfiguration configuration = processEngineFactoryBean.getProcessEngineConfiguration ();
        return diagramGenerator.generateDiagram (bpmnModel, "png", executedActivityIdList, highLightedFlows,
                configuration.getActivityFontName (), configuration.getLabelFontName (),
                "宋体", null, 1.0, currentActivityIdList);

    }

    private List<String> getHighLightedFlows (ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> hightFlows = new ArrayList<> (1);  //用以保存高亮的线flowId
        int size = historicActivityInstanceList.size (), _size = size - 1;
        for(int i=0; i<_size; i++) { // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            ActivityImpl activityImpl = processDefinitionEntity.findActivity (historicActivityInstanceList.get (i).getActivityId ()) ;

            List<ActivityImpl> activityList = new ArrayList<> (1);// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl = processDefinitionEntity.findActivity (historicActivityInstanceList.get (i + 1).getActivityId ()) ;

            // 将后面第一个节点放在时间相同节点的集合里
            activityList.add (sameActivityImpl);

            for(int j= i + 1; j < _size; j++ ) {
                // 后续第一个和第二个节点
                HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get (j),
                        historicActivityInstance1 = historicActivityInstanceList.get (j+1);

                if(historicActivityInstance.getStartTime ().equals (historicActivityInstance1.getStartTime ())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl _activityImpl = processDefinitionEntity.findActivity (historicActivityInstance1.getActivityId ());
                    activityList.add (_activityImpl);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }

            List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions ();// 取出节点的所有出去的线
            for(PvmTransition pv : pvmTransitionList) {
                // 对所有的线进行遍历
                ActivityImpl _activityImpl = (ActivityImpl) pv.getDestination ();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if(activityList.contains (_activityImpl)) {
                    hightFlows.add (pv.getId ());
                }
            }

        }
        return hightFlows;
    }
}
