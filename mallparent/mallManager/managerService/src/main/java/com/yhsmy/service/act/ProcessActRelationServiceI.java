package com.yhsmy.service.act;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.ProcessActRelation;
import com.yhsmy.entity.vo.sys.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/20 15:27
 **/
public interface ProcessActRelationServiceI {

    /**
     * 根据条件获取列表信息
     *
     * @param queryParams
     * @return
     */
    public DataGrid getProcessActRelationList (QueryParams queryParams);

    /**
     * @return
     */
    public Map<String, Object> getForm ();

    /**
     * 保存流程KEY与流程图的关联关系
     *
     * @param processActRelation
     * @return
     */
    public Json formSubmit (ProcessActRelation processActRelation, User user);

    /**
     * 删除流程KEY与流程图的关联关系
     *
     * @param id
     * @return
     */
    public Json deleteProcessActRelation (String id);

    /**
     * 保存activiti model数据
     *
     * @param processActId
     * @return
     */
    public String getModelForm (String processActId) throws Exception;

    /**
     * 部署流程
     *
     * @param id
     * @return
     */
    public Json deployProcess (String id);

    /**
     * 同步用户数据到activiti中
     * @return
     */
    public Json syncData ();
}
