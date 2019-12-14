package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.Depart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/29 18:29
 **/
public interface DepartMapper {

    /**
     * 保存部门数据
     *
     * @param depart
     */
    public void addDepart (Depart depart);

    /**
     * @param queryBy   0=忽略 >0=按部门名称
     * @param queryText
     * @param pid       上级ID
     * @return
     */
    public List<Depart> findDepartList (@Param("queryBy") int queryBy, @Param("queryText") String queryText, @Param("pid") String pid);

    /**
     * 按ID查询数据
     *
     * @param id
     * @return
     */
    public Depart findDepartById (@Param("id") String id);

}
