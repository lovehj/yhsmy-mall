package com.yhsmy.entity.vo.act;

import com.yhsmy.enums.NormalEnum;

/**
 * @auth 李正义
 * @date 2020/1/9 21:35
 **/
public class BusinessAudit extends com.yhsmy.entity.act.BusinessAudit {

    private static final long serialVersionUID = 6694764276364759015L;

    public BusinessAudit (String businessId, String businessKey, String processInstanceId) {
        super.setBusinessId (businessId);
        super.setBusinessKey (businessKey);
        super.setProcessInstanceId (processInstanceId);
        super.setRemark ("");
        super.setAuditState (NormalEnum.NORMAL.getKey ());
    }
}
