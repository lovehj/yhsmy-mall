package com.yhsmy.service.act;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.AuditRecord;
import com.yhsmy.entity.vo.act.Leave;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/20 18:04
 **/
public interface LeaveServiceI {

    /**
     * 请假数据列表
     *
     * @param params
     * @param user
     * @return
     */
    public DataGrid getLeaveList (int state, QueryParams params, User user);

    /**
     * @param leaveId 请假ID
     * @return
     */
    public Map<String, Object> getForm (String leaveId);

    /**
     * 编辑请假
     *
     * @param leave 请假对象
     * @param user  当前操作的用户
     * @return
     */
    public Json formSubmit (Leave leave, User user);

    /**
     * 删除请假
     *
     * @param leaveId 请假ID
     * @param user    当前操作的用户
     * @return
     */
    public Json delete (String leaveId, User user);

    /**
     * 根据流程实例ID查询审批记录
     *
     * @param procesInstanceId
     * @return
     */
    public List<AuditRecord> getLeaveAuditRecord (String procesInstanceId);
}
