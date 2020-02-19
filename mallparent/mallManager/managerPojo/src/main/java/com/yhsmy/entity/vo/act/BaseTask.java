package com.yhsmy.entity.vo.act;

import java.io.Serializable;

/**
 * @auth 李正义
 * @date 2019/12/24 22:12
 **/
public abstract class BaseTask implements Serializable {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id.trim();
    }

    protected String userId;

    protected String username;

    protected String processInstanceId;

    protected String content;

    protected String state;

    protected String creator;

    protected String createTime;

    protected String modifyor;

    protected String modifyTime;

    protected String urlPath;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getProcessInstanceId () {
        return processInstanceId;
    }

    public void setProcessInstanceId (String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getContent () {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public String getCreator () {
        return creator;
    }

    public void setCreator (String creator) {
        this.creator = creator;
    }

    public String getCreateTime () {
        return createTime;
    }

    public void setCreateTime (String createTime) {
        this.createTime = createTime;
    }

    public String getModifyor () {
        return modifyor;
    }

    public void setModifyor (String modifyor) {
        this.modifyor = modifyor;
    }

    public String getModifyTime () {
        return modifyTime;
    }

    public void setModifyTime (String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUrlPath () {
        return urlPath;
    }

    public void setUrlPath (String urlPath) {
        this.urlPath = urlPath;
    }
}
