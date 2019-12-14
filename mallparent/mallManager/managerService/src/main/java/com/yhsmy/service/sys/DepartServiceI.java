package com.yhsmy.service.sys;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Depart;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/29 17:54
 **/
public interface DepartServiceI {

    /**
     * 获取部门列表
     *
     * @param combRelation true=组合部门父子关系
     * @return 部门集合
     */
    public List<Depart> getDepartList (boolean combRelation);

    /**
     * 获取部门信息
     *
     * @param id 部门ID
     * @return
     */
    public Map<String, Object> getForm (String id);

    /**
     * 编辑部门信息
     *
     * @param depart 部门对象
     * @param user   操作的用户
     * @return
     */
    public Json formSubmit (Depart depart, User user);

    /**
     * 删除部门
     *
     * @param id   部门ID
     * @param user 操作的用户
     * @return
     */
    public Json delete (String id, User user);
}
