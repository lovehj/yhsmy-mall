package com.yhsmy.service.act;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.AuditRecord;
import com.yhsmy.entity.vo.sys.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/24 21:43
 **/
public interface WaitTaskServiceI {

    /**
     * 待办任务
     *
     * @param queryParams 查询条件
     * @param user        当前用户
     * @return
     */
    public DataGrid getListDate (QueryParams queryParams, User user);

    /**
     * 查询任务信息
     *
     * @param taskId            任务ID
     * @param processInstanceId 流程实例ID
     * @return
     */
    public Map<String, Object> getAgent (String taskId, String processInstanceId);

    /**
     * 完成审批任务
     *
     * @param auditRecord
     * @param user
     * @return
     */
    public Json complete (AuditRecord auditRecord, User user);

    /**
     * 根据流程的KEY值，启动任务
     *
     * @param businessId  业务ID
     * @param startKey    启动流程的KEY值
     * @param stateFields 更新的状态字段
     * @return
     */
    public Json startTask (String businessId, String startKey, String stateFields);

    /**
     * 根据流程实例ID查询记录
     *
     * @param processInstanceId
     * @return
     */
    public List<AuditRecord> getAuditDetail (String processInstanceId);

    /**
     * 查询已办理完结的任务
     *
     * @param queryParams
     * @param user
     * @return
     */
    public DataGrid getFinishedListData (QueryParams queryParams, User user);

    /**
     * 根据启动的KEY和流程实例ID获取数据
     *
     * @param startKey
     * @param processInstanceId
     * @return
     */
    public String getIframeUrl (String startKey, String processInstanceId);
}
