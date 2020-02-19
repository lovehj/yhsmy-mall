package com.yhsmy.entity.act;

import lombok.Data;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2020/1/9 21:29
 **/
@Data
public class BusinessAudit implements Serializable {

    private static final long serialVersionUID = 7180817772365456458L;
    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 流程启动的KEY值
     */
    private String businessKey;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    private String remark;

    private int auditState;

}
