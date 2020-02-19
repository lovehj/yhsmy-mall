package com.yhsmy.entity.vo.act;

import com.yhsmy.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auth 李正义
 * @date 2019/12/20 15:07
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessActRelation extends com.yhsmy.entity.act.ProcessActRelation {
    private static final long serialVersionUID = -3895714228409791043L;

    /**
     * 数据
     */
    private String metaInfo;

    /**
     * 当前版本号
     */
    private int version;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * XML 原文件ID
     */
    private String xmlSourceId;

    /**
     * 流程图 ID
     */
    private String imgSourceId;

    public String getCreateTimeStr() {
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }
}
