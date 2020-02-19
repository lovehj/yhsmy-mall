package com.yhsmy.entity.vo.act;

import lombok.Data;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2019/12/23 16:25
 **/
@Data
public class ProcessDefinitionExt implements Serializable {

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private String tenantId;

}
