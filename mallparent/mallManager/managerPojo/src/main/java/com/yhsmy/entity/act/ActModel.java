package com.yhsmy.entity.act;

import lombok.Data;
import org.activiti.engine.repository.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @auth 李正义
 * @date 2019/12/15 9:39
 **/
@Data
public class ActModel implements Serializable {

    private String id;
    private String name;
    private String key;
    private String category;
    private Date createTime;
    private Date lastUpdateTime;
    private Integer version;
    private String metaInfo;
    private String deploymentId;
    private String tenantId;
    private boolean hasEditorSource;

    public ActModel() {
    }

    public ActModel(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.key = model.getKey();
        this.category = model.getCategory();
        this.createTime = model.getCreateTime();
        this.lastUpdateTime = model.getLastUpdateTime();
        this.version = model.getVersion();
        this.metaInfo = model.getMetaInfo();
        this.deploymentId = model.getDeploymentId();
        this.tenantId = model.getTenantId();
        this.hasEditorSource = model.hasEditorSource();
    }

}
