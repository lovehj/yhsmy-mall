package com.yhsmy.service.act.impl;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.RabbitMqMessage;
import com.yhsmy.entity.vo.act.AuditRecord;
import com.yhsmy.entity.vo.act.BusinessAudit;
import com.yhsmy.entity.vo.act.Leave;
import com.yhsmy.entity.vo.act.TaskExt;
import com.yhsmy.entity.vo.mall.Content;
import com.yhsmy.entity.vo.mall.Item;
import com.yhsmy.entity.vo.sys.Message;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.act.BusinessAuditMapper;
import com.yhsmy.mapper.act.LeaveMapper;
import com.yhsmy.mapper.mall.ContentMapper;
import com.yhsmy.mapper.mall.ItemMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.mapper.sys.UserMapper;
import com.yhsmy.service.act.WaitTaskServiceI;
import com.yhsmy.service.sys.MessageServiceI;
import com.yhsmy.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @auth 李正义
 * @date 2019/12/24 21:43
 **/
@Service
@Transactional(readOnly = true)
@Slf4j
public class WaitTaskServiceImpl implements WaitTaskServiceI {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MessageServiceI messageServiceI;

    @Autowired
    private BusinessAuditMapper businessAuditMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    public DataGrid getListDate (QueryParams queryParams, User user) {
        String userId = user.getId (), roleId = user.getRoleId ();
        int startRow = queryParams.getStartRow (), pageSize = queryParams.getPageSize ();
        int queryBy = queryParams.getQueryBy ();
        String queryText = queryParams.getQueryText ();

        TaskQuery assigneeQuery = taskService.createTaskQuery ().taskAssignee (userId),
                candidateQuery = taskService.createTaskQuery ().taskCandidateUser (userId),
                candidateGroupQuery = taskService.createTaskQuery ().taskCandidateGroup (roleId);

        if (queryBy > 0 && StringUtils.isNotBlank (queryText)) {
            assigneeQuery.processDefinitionKeyLike ("%" + queryText + "%");
            candidateQuery.processDefinitionKeyLike ("%" + queryText + "%");
            candidateGroupQuery.processDefinitionKeyLike ("%" + queryText + "%");
        }

        long count = assigneeQuery.count () + candidateQuery.count () + candidateGroupQuery.count ();

        // 单个办理人任务
        List<Task> assigneeList = assigneeQuery.listPage (startRow, pageSize);
        // 候选人任务
        List<Task> candidateList = candidateQuery.listPage (startRow, pageSize);
        // 组任务
        List<Task> candidataGroupList = candidateGroupQuery.listPage (startRow, pageSize);

        if (candidateList.size () > 0) {
            assigneeList.addAll (candidateList);
        }

        if (candidataGroupList.size () > 0) {
            assigneeList.addAll (candidataGroupList);
        }

        List<TaskExt> taskExtList = new ArrayList<> (assigneeList.size ());
        Set<String> taskSet = new HashSet<> (1);
        for (Task task : assigneeList) {
            String taskId = task.getId ();
            if (taskSet.contains (taskId)) {
                continue;
            }
            taskSet.add (taskId);
            TaskExt taskExt = new TaskExt (task);
            // 判断任务的类型
            if (StringUtils.isNotBlank (taskExt.getProcDefId ())) {
                String[] procDefIds = StringUtils.split (taskExt.getProcDefId (), ":");
                if (procDefIds != null && procDefIds.length > 0) {
                    String key = procDefIds[0];
                    if (ApproveEnum.LEAVEBILL.getKey ().indexOf (key) > -1) {
                        taskExt.setAuditCtypeName (ApproveEnum.LEAVEBILL.getValue ());
                        taskExt.setAuditCtypeKey (ApproveEnum.LEAVEBILL.getKey ());
                        this.setTaskExt (taskExt, task.getProcessInstanceId (), ApproveEnum.LEAVEBILL.getKey ());
                    } else if (ApproveEnum.ITEMAUDIT.getKey ().indexOf (key) > -1) {
                        taskExt.setAuditCtypeKey (ApproveEnum.ITEMAUDIT.getKey ());
                        taskExt.setAuditCtypeName (ApproveEnum.ITEMAUDIT.getValue ());
                        this.setTaskExt (taskExt, task.getProcessInstanceId (), ApproveEnum.ITEMAUDIT.getKey ());
                    } else if (ApproveEnum.CONTENT.getKey ().indexOf (key) > -1) {
                        taskExt.setAuditCtypeKey (ApproveEnum.CONTENT.getKey ());
                        taskExt.setAuditCtypeName (ApproveEnum.CONTENT.getValue ());
                        this.setTaskExt (taskExt, task.getProcessInstanceId (), ApproveEnum.CONTENT.getKey ());
                    }
                }
            }
            taskExtList.add (taskExt);
        }
        return new DataGrid (count, taskExtList);
    }

    private void setTaskExt (TaskExt taskExt, String processId, String approveKey) {
        if (StringUtils.isEmpty (approveKey)) {
            if (StringUtils.isNotEmpty (taskExt.getStartKey ())) {
                approveKey = taskExt.getStartKey ();
            }
        }
        // 从业务审批记录表中查询记录
        BusinessAudit businessAudit = businessAuditMapper.findBussinessAudit (null, null, processId);
        if (businessAudit == null || StringUtils.isBlank (businessAudit.getBusinessId ())) {
            return;
        }

        String bussinessId = businessAudit.getBusinessId ();

        if (ApproveEnum.LEAVEBILL.getKey ().indexOf (approveKey) > -1) {
            Leave leave = leaveMapper.findLeaveById (bussinessId);
            if (leave == null) {
                return;
            }
            taskExt.setAuditName (leave.getCreator ());
            taskExt.setAuditContent (leave.getContent ());
        } else if (ApproveEnum.ITEMAUDIT.getKey ().indexOf (approveKey) > -1) {
            Item item = itemMapper.findItemById (bussinessId);
            if (item == null) {
                return;
            }
            taskExt.setAuditName (item.getCreator ());
            taskExt.setAuditContent (item.getTitle () + " | " + item.getSellPoint ());
        } else if (ApproveEnum.CONTENT.getKey ().indexOf (approveKey) > -1) {
            Content content = contentMapper.findContentById (bussinessId);
            if (content == null) {
                return;
            }
            taskExt.setAuditName (content.getCreator ());
            taskExt.setAuditContent (content.getTitle () + " | " + content.getSubTitle ());
        }
    }

    @Override
    public Map<String, Object> getAgent (String taskId, String processInstanceId) {
        if (StringUtils.isBlank (taskId) || StringUtils.isBlank (processInstanceId)) {
            throw new PageNotFoundException ();
        }

        Map<String, Object> result = new HashMap<> (1);
        Task task = taskService.createTaskQuery ().taskId (taskId).singleResult ();
        if (task == null || !task.getProcessInstanceId ().equals (processInstanceId)) {
            throw new PageNotFoundException ();
        }

        String iframeUrl = "";
        String[] procDefIds = StringUtils.split (task.getProcessDefinitionId (), ":");
        if (procDefIds != null && procDefIds.length > 0) {
            iframeUrl = this.getIframeUrl (procDefIds[0], task.getProcessInstanceId ());
        } else {
            throw new PageNotFoundException ("任务的流程定义ID为空!");
        }

        result.put ("iframeUrl", iframeUrl);
        result.put ("taskId", taskId);
        return result;
    }

    public String getIframeUrl (String key, String procInstanceId) {
        if (ApproveEnum.LEAVEBILL.getKey ().indexOf (key) > -1) {
            Leave leave = leaveMapper.findLeaveListByProcessInstanceId (procInstanceId);
            if (leave == null || StringUtils.isEmpty (leave.getLeaveId ())) {
                throw new PageNotFoundException ();
            }
            return "/leave/view?noCloseBtn=true&id=" + leave.getLeaveId ();
        } else if (ApproveEnum.ITEMAUDIT.getKey ().indexOf (key) > -1) {
            Item item = itemMapper.findItemByProcessInstanceId (procInstanceId);
            if (item == null || StringUtils.isEmpty (item.getItemId ())) {
                throw new PageNotFoundException ();
            }
            return "/item/view?noCloseBtn=true&id=" + item.getItemId ();
        } else if (ApproveEnum.CONTENT.getKey ().indexOf (key) > -1) {
            BusinessAudit businessAudit = businessAuditMapper.findBussinessAudit (null, null, procInstanceId);
            if (businessAudit != null && StringUtils.isNotBlank (businessAudit.getBusinessId ())) {
                Content content = contentMapper.findContentById (businessAudit.getBusinessId ());
                if (content == null || StringUtils.isBlank (content.getContentId ())) {
                    throw new PageNotFoundException ();
                }
                return "/content/view?noCloseBtn=true&id=" + content.getContentId ();


            }
        }
        return "";
    }

    @Override
    @Transactional(readOnly = false)
    public Json complete (AuditRecord auditRecord, User user) {
        String original = auditRecord.getOpinion ();
        boolean flag = auditRecord.isFlag ();
        Task task = taskService.createTaskQuery ().taskId (auditRecord.getTaskId ()).singleResult ();
        if (task == null || StringUtils.isBlank (task.getProcessDefinitionId ()) || task.getProcessDefinitionId ().indexOf (":") <= -1) {
            return Json.fail ();
        }

        String[] procDefIds = StringUtils.split (task.getProcessDefinitionId (), ":");
        Map<String, Object> taskVariableMap = new HashMap<> (1);

        Object[] variables = setTaskVariables (procDefIds[0], task.getProcessInstanceId (), original, flag, taskVariableMap, user);
        Message message = (Message) variables[0];
        RabbitMqMessage rabbitMqMessage = (RabbitMqMessage) variables[1];
        if (message == null || rabbitMqMessage == null) {
            return Json.fail ();
        }

        taskService.complete (task.getId (), taskVariableMap);

        // 更新业务审批记录表的值
        UpdateMap updateMap = new UpdateMap ("smy_business_audit");
        updateMap.addField ("remark", (String) variables[3]);
        updateMap.addWhere ("businessId", (String) variables[2]);
        if (this.mybatisMapper.update (updateMap) <= 0) {
            throw new ServiceException ("");
        }

        // 判断流程是否已执行结束
        String processInstanceId = task.getProcessInstanceId ();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ().processInstanceId (processInstanceId).singleResult ();
        if (processInstance == null) { //说明流程已执行结束
            // 进一步验证流程已结束
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery ().processInstanceId (processInstanceId).singleResult ();
            if (historicProcessInstance.getEndTime () != null) {
                rabbitMqMessage.setSendMessage (true);
                Map<String, String> dbOperaFieldsMap = rabbitMqMessage.getDbFieldsMap ();
                // 更新表的审批状态
                dbOperaFieldsMap.put ("state", String.valueOf (NormalEnum.AUDIT_PASS.getKey ()));
                if (!flag) {
                    dbOperaFieldsMap.put ("state", String.valueOf (NormalEnum.AUDIT_BACK.getKey ()));
                }

                // 保存数据到消息表中
                messageServiceI.addMessage (message);
            }
        }
        return Json.ok (rabbitMqMessage);
    }

    private Object[] setTaskVariables (String key, String procInstanceId, String original, boolean flag, Map<String, Object> taskVariableMap, User user) {
        BusinessAudit businessAudit = businessAuditMapper.findBussinessAudit (null, null, procInstanceId);
        if (businessAudit == null || StringUtils.isBlank (businessAudit.getBusinessId ())) {
            throw new ServiceException ("相关审批记录未找到!");
        }
        Map<String, String> dbOperaFieldsMap = new HashMap<> (1),
                dbOperaWhereMap = new HashMap<> (1);

        original = (StringUtils.isEmpty (businessAudit.getRemark ()) ? "" : businessAudit.getRemark () + " | ") + original;
        if (original.length () > 300) {
            original = original.substring (0, 300);
        }
        String businessId = businessAudit.getBusinessId (), title = null, tableName = "";
        Message message = null;
        RabbitMqMessage rabbitMqMessage = null;
        if (ApproveEnum.LEAVEBILL.getKey ().indexOf (key) > -1) {
            Leave leave = leaveMapper.findLeaveById (businessId);
            if (leave == null || StringUtils.isEmpty (leave.getLeaveId ())) {
                throw new ServiceException ("未找到相关请假!");
            }
            taskVariableMap.put ("days", Double.parseDouble (leave.getDays ()));
            taskVariableMap.put ("personnelAudit", flag);
            title = "请假审批";
            message = new Message (leave.getUserId (), title, "您的" + title + (flag ? "已" : "未") + "通过!", "/leave/view?id=" + businessId);
            dbOperaWhereMap.put ("leaveId", businessId);
            tableName = "smy_leave";
        } else if (ApproveEnum.ITEMAUDIT.getKey ().indexOf (key) > -1) {
            Item item = itemMapper.findItemById (businessId);
            if (item == null || StringUtils.isEmpty (item.getProcessInstanceId ())) {
                throw new ServiceException ("相关商品未找到!");
            }
            taskVariableMap.put ("financerAudit", flag);
            taskVariableMap.put ("managerAudit", flag);
            title = "商品审批";
            message = new Message (item.getUserId (), title, "您提交的" + title + (flag ? "已" : "未") + "通过!", "/item/view?id=" + businessId);
            dbOperaWhereMap.put ("itemId", businessId);
            tableName = "smy_item";
        } else if (ApproveEnum.CONTENT.getKey ().indexOf (key) > -1) {
            Content c = contentMapper.findContentById (businessId);
            if (c == null) {
                throw new ServiceException ("未找到相关内容!");
            }
            taskVariableMap.put ("contentAdminAudit", flag);
            taskVariableMap.put ("leftManagerAudit", flag);
            title = "内容审批";
            message = new Message (c.getUserId (), title, "您提交的" + title + (flag ? "已" : "未") + "通过!", "/content/view?id=" + businessId);
            tableName = "smy_content";
            dbOperaWhereMap.put ("contentId", businessId);
        }
        rabbitMqMessage = new RabbitMqMessage (user, false, 1, title, message.getContent (), null, true, 0, tableName, dbOperaFieldsMap, dbOperaWhereMap);
        return new Object[]{message, rabbitMqMessage, businessId, original};
    }

    @Override
    @Transactional(readOnly = false)
    public Json startTask (String businessId, String startKey, String stateFields) {
        if (StringUtils.isBlank (stateFields)) {
            stateFields = "state";
        }
        // 检查表是否存在
        String tableIdFields = ApproveEnum.getTableIdFieldsByKey (startKey),
                tableName = ApproveEnum.getTableNameByKey (startKey);
        if (StringUtils.isBlank (tableIdFields) || StringUtils.isBlank (tableName)) {
            return Json.fail ("审批的业务表不存在!");
        }

        // 查询当前ID的审批记录是否存在
        BusinessAudit existsBusinessAudit = businessAuditMapper.findBussinessAudit (businessId, null, null);
        if (existsBusinessAudit != null) {
            if (StringUtils.isNotBlank (existsBusinessAudit.getProcessInstanceId ()) ||
                    !startKey.equalsIgnoreCase (existsBusinessAudit.getBusinessKey ()))
                return Json.fail ("当前审批记录已存在或启动流程的关键词不相同!");
        }

        ProcessInstance pi = runtimeService.startProcessInstanceByKey (startKey);
        if (pi == null || StringUtils.isBlank (pi.getId ())) {
            return Json.fail ("流程启动失败!");
        }

        String procInstId = pi.getProcessInstanceId ();
        if (existsBusinessAudit != null) {
            // 更新业务审批记录表
            UpdateMap updateMap = new UpdateMap ("smy_business_audit");
            updateMap.addField ("processInstanceId", procInstId);
            updateMap.addField ("auditState", NormalEnum.NORMAL.getKey ());
            updateMap.addWhere ("businessId", procInstId);
            updateMap.addWhere ("businessKey", startKey);
            if (mybatisMapper.update (updateMap) <= 0) {
                throw new ServiceException ("更新业务审批表记录失败!");
            }
        } else {
            businessAuditMapper.addBusinessAudit (new BusinessAudit (businessId, startKey, procInstId));
        }

        // 更新业务表的状态值
        String stateStr = NormalEnum.NORMAL.getKey () + "," + NormalEnum.AUDIT_PASS.getKey () + "," + NormalEnum.AUDIT_BACK.getKey ();
        UpdateMap updateMap = new UpdateMap (tableName);
        updateMap.addField (stateFields, NormalEnum.AUDITING.getKey ());
        updateMap.addWhere (tableIdFields, businessId);
        updateMap.addWhere (stateFields, stateStr, "in");
        if (mybatisMapper.update (updateMap) <= 0) {
            throw new ServiceException ("业务状态更新失败!");
        }
        return Json.ok ();
    }

    @Override
    public List<AuditRecord> getAuditDetail (String processInstanceId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery () //
                .processInstanceId (processInstanceId).finished ().list ();
        BusinessAudit businessAudit = businessAuditMapper.findBussinessAudit (null, null, processInstanceId);
        String[] remarks = null;
        if (businessAudit != null && StringUtils.isNotEmpty (businessAudit.getRemark ())) {
            remarks = StringUtils.split (businessAudit.getRemark (), "|");
        }
        int size = historicTaskInstances.size ();
        List<AuditRecord> auditRecordList = new ArrayList<> (size);
        for (int i = 0; i < size; i++) {
            HistoricTaskInstance hi = historicTaskInstances.get (i);
            AuditRecord record = new AuditRecord ();
            record.setTaskName (hi.getName ());
            record.setAssgin ("");
            User assginUser = userMapper.findUserById (hi.getAssignee ());
            if (assginUser != null && StringUtils.isNotEmpty (assginUser.getRealName ())) {
                record.setAssgin (assginUser.getRealName ());
            }
            record.setStartTime (DateTimeUtil.formatDate (hi.getStartTime ()));
            record.setEndTime (DateTimeUtil.formatDate (hi.getEndTime ()));
            record.setDuration (DateTimeUtil.durationForDays (hi.getDurationInMillis ()));
            if (remarks != null) {
                record.setOpinion (remarks[i]);
            }
            auditRecordList.add (record);
        }
        return auditRecordList;
    }

    @Override
    public DataGrid getFinishedListData (QueryParams queryParams, User user) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery ();
        if (!user.isAdmin ()) {
            historicTaskInstanceQuery.taskAssignee (user.getId ());
        }

        if (StringUtils.isNotEmpty (queryParams.getQueryText ())) {
            historicTaskInstanceQuery.processDefinitionKeyLikeIgnoreCase ("%" + queryParams.getQueryText () + "%");
        }
        historicTaskInstanceQuery = historicTaskInstanceQuery.finished ();
        DataGrid dataGrid = new DataGrid ();
        dataGrid.setCount (historicTaskInstanceQuery.count ());
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.listPage (queryParams.getStartRow (), queryParams.getPageSize ());
        List<TaskExt> taskExtList = new ArrayList<> (historicTaskInstances.size ());
        for (HistoricTaskInstance taskInstance : historicTaskInstances) {
            TaskExt taskExt = new TaskExt (taskInstance);
            taskExt.setAssignee ("");
            User assigneeUser = userMapper.findUserById (taskInstance.getAssignee ());
            if (assigneeUser != null) {
                taskExt.setAssignee (assigneeUser.getRealName ());
            }
            setTaskExt (taskExt, taskInstance.getProcessInstanceId (), null);
            taskExtList.add (taskExt);
        }
        dataGrid.setData (taskExtList);
        return dataGrid;
    }


    /**
     * 强制结束一个正在运行中的任务
     *
     * @param processDefinintionID 流程定义ID,示例格式：leaveBill:1:50004
     * @param taskId               任务ID
     */
    public void stopRuningTask (String processDefinintionID, String taskId) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery ().processDefinitionKey ("leaveBill").singleResult ();
        ProcessDefinitionEntity pdEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition (pd.getId ());
        ;
        List<ActivityImpl> activities = pdEntity.getActivities ();
        String endId = null;
        for (ActivityImpl ai : activities) {
            String type = ai.getProperty ("type").toString ();
            if (type.equals ("endEvent")) {
                endId = ai.getId ();
                break;
            }
        }

        Task task = taskService.createTaskQuery ().processInstanceId (taskId).singleResult ();
        TaskServiceImpl timpl = (TaskServiceImpl) taskService;
        timpl.getCommandExecutor ().execute (new JumpTaskCmdService (task.getExecutionId (), endId));
    }

}
