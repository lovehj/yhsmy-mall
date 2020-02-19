package com.yhsmy.service.act.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.AuditRecord;
import com.yhsmy.entity.vo.act.Leave;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.act.LeaveMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.act.LeaveServiceI;
import com.yhsmy.utils.UUIDUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/20 18:04
 **/
@Service
@Transactional(readOnly = true)
public class LeaveServiceImpl implements LeaveServiceI {

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    private String leaveOpinionList = "leaveOpinionList";

    @Override
    public DataGrid getLeaveList (int state, QueryParams params, User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = "";
        }
        Page<Leave> page = PageHelper.startPage (params.getPageNo (), params.getPageSize ());
        List<Leave> leaveList = leaveMapper.findLeaveList (userId, state, params.getQueryBy (), params.getQueryText (),
                params.getQueryDate (), params.getStartDate (), params.getEndDate ());
        return new DataGrid (page.getTotal (), leaveList);
    }

    @Override
    public Map<String, Object> getForm (String leaveId) {
        Leave leave = null;
        boolean leaveEditFlag = true;
        if (StringUtils.isNotBlank (leaveId)) {
            leave = leaveMapper.findLeaveById (leaveId);
            if (leave == null) {
                throw new PageNotFoundException ();
            }
            int state = leave.getState ();
            // 审批中或审批通过的记录不可以再被编辑
            if (state == NormalEnum.AUDIT_PASS.getKey () || state == NormalEnum.AUDITING.getKey ()) {
                leaveEditFlag = false;
            }
        } else {
            leave = new Leave ();
            leave.setLeaveId ("");
            leave.setDays ("1");
            leave.setContent ("");
            leave.setStartDate (null);
            leave.setEndDate (null);
            leave.setRemark ("");
        }
        Map<String, Object> result = new HashMap<> (1);
        result.put ("leave", leave);
        result.put ("leaveEditFlag", leaveEditFlag);
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (Leave leave, User user) {
        String cname = user.getRealName (), userId = user.getId (), leaveId = leave.getLeaveId ();
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isNotBlank (leaveId)) {
            UpdateMap updateMap = new UpdateMap ("smy_leave");
            updateMap.addField ("days", leave.getDays ());
            updateMap.addField ("content", leave.getContent ());
            updateMap.addField ("startDate", leave.getStartDate ());
            updateMap.addField ("endDate", leave.getEndDate ());
            updateMap.addWhere ("state", NormalEnum.NORMAL.getKey ());
            updateMap.addWhere ("leaveId", leaveId);
            if (user.getCtype () != 1) {
                updateMap.addWhere ("userId", userId);
            }
            if (mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            leave.setLeaveId (UUIDUtil.generateUUID ());
            leave.setUserId (userId);
            leave.setProcessInstanceId ("");
            leave.setState (NormalEnum.NORMAL.getKey ());
            leave.setCreator (cname);
            leave.setCreateTime (now);
            leave.setModifyor (cname);
            leave.setModifyTime (now);
            leaveMapper.addLeave (leave);
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public Json delete (String leaveId, User user) {
        // 删除时不能删除审批中的记录
        String stateStr = NormalEnum.NORMAL.getKey () + "," + NormalEnum.AUDIT_PASS.getKey () + "," + NormalEnum.AUDIT_BACK.getKey ();
        UpdateMap delMap = new UpdateMap ("smy_leave");
        delMap.addField ("modifyor", user.getRealName ());
        delMap.addField ("modifyTime", LocalDateTime.now ());
        delMap.addField ("state", NormalEnum.DELETE.getKey ());
        delMap.addWhere ("leaveId", leaveId);
        delMap.addWhere ("state", stateStr, "in");
        if (mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    public List<AuditRecord> getLeaveAuditRecord (String procesInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ()//
                .processInstanceId (procesInstanceId).singleResult ();

        List<AuditRecord> auditRecordList = null;
        List<HistoricActivityInstance> historyActInstanceList = new ArrayList<> (1);

        if(processInstance != null) {
            Task task = taskService.createTaskQuery () //
                    .processInstanceId (procesInstanceId).singleResult ();

            Map<String, Object> variables = taskService.getVariables (task.getId ());
            Object obj = variables.get (leaveOpinionList);
            if(obj != null ){
                auditRecordList = (List<AuditRecord>) obj;
            }
        } else {
            auditRecordList = new ArrayList<> ();
            List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery () //
                .processInstanceId (procesInstanceId).list ();

            HistoricVariableUpdate variables = null;
            for(HistoricDetail hd : historicDetails) {
                variables = (HistoricVariableUpdate) hd;
                String varableName = variables.getVariableName ();
                if(leaveOpinionList.equals (varableName)) {
                    auditRecordList.clear ();
                    auditRecordList.addAll ((List<AuditRecord>) variables.getValue());
                }
            }
        }
        return auditRecordList;
    }
}
