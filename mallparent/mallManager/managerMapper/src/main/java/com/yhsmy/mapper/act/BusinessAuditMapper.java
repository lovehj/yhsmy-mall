package com.yhsmy.mapper.act;

import com.yhsmy.entity.vo.act.BusinessAudit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/9 21:38
 **/
public interface BusinessAuditMapper {

    /**
     * 保存业务审批关系
     *
     * @param businessAudit
     */
    public void addBusinessAudit (BusinessAudit businessAudit);

    /**
     * 查询业务审批关系
     *
     * @param bussinessId
     * @param startKey
     * @return
     */
    public List<BusinessAudit> findBussinessAuditList (@Param("bussinessId") String bussinessId, @Param("startKey") String startKey);

    /**
     * 根据业务ID或流程ID查询
     *
     * @param bussinessId       业务ID和startKey是一对查询条件
     * @param startKey          启动流程的KEY
     * @param processInstanceId 流程实例ID
     * @return
     */
    public BusinessAudit findBussinessAudit (@Param("bussinessId") String bussinessId, @Param("startKey") String startKey, @Param("processInstanceId") String processInstanceId);

}
