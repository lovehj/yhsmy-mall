package com.yhsmy.mapper.act;

import com.yhsmy.entity.vo.act.Leave;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/20 17:21
 **/
public interface LeaveMapper {

    /**
     * 保存请假
     *
     * @param leave
     */
    public void addLeave (Leave leave);

    /**
     * 查询请假信息
     *
     * @param userId    null=忽略
     * @param state     0=忽略 1=正常 10=审批中 11=审批通过 12=审批退回
     * @param queryBy   0=忽略 1=请假人 2=请假事由 3=审批备注
     * @param queryText null=忽略
     * @param queryDate 0=忽略 1=请假开始时间 2=请假结束时间 3=创建时间
     * @param startDate null=忽略
     * @param endDate   null=忽略
     * @return List
     */
    public List<Leave> findLeaveList (@Param("userId") String userId, @Param("state") int state, @Param("queryBy") int queryBy, @Param("queryText") String queryText,
                                      @Param("queryDate") int queryDate, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 根据请假ID查询
     *
     * @param leaveId
     * @return
     */
    public Leave findLeaveById (@Param("leaveId") String leaveId);

    /**
     * 根据流程实例查询请假对象
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    public Leave findLeaveListByProcessInstanceId (@Param("processInstanceId") String processInstanceId);

}
