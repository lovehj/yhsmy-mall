package com.yhsmy.mapper.act;

import com.yhsmy.entity.vo.act.ProcessActRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/20 15:08
 **/
public interface ProcessActRelationMapper {

    /**
     * 保存流程图与流程KEY之间的关联关系
     *
     * @param processActRelation
     */
    public void addProcessActRelation (ProcessActRelation processActRelation);

    /**
     * 根据条件查询流程KEY与流程图的关联关系
     *
     * @param queryBy   0=忽略 1=流程KEY 2=流程名称 3=流程备注
     * @param queryText
     * @param queryDate 0=忽略 1=创建时间
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param leftModel true=连接ACT_RE_MODEL false=不连接
     * @return List
     */
    public List<ProcessActRelation> findProcessActRelationList (@Param("queryBy") int queryBy, @Param("queryText") String queryText,
                                                                @Param("queryDate") int queryDate, @Param("startDate") String startDate,
                                                                @Param("endDate") String endDate, @Param("leftModel") boolean leftModel);

    /**
     * 根据ID查询流程KEY与流程图的关联关系
     *
     * @param id
     * @return
     */
    public ProcessActRelation findProcessActRelationById (@Param("id") String id);

}
