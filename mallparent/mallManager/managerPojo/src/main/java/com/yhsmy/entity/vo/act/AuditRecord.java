package com.yhsmy.entity.vo.act;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批记录的包装类
 *
 * @auth 李正义
 * @date 2019/12/24 18:31
 **/
@Data
public class AuditRecord implements Serializable {

    //审批人id
    private String taskId;

    //审批人姓名
    private String taskName;

    //审批意见
    private String opinion;

    //审批时间
    private Date createTime;

    //是否通过
    private boolean flag;

    public String getCreateTimeStr() {
        if(this.createTime == null) {
            return "";
        }
        return DateTimeUtil.formatDate(this.createTime);
    }

    /**
     * 办理人
     */
    private String assgin;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 历经时长
     */
    private String duration;



}
