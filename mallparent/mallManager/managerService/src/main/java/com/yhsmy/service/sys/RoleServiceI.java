package com.yhsmy.service.sys;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Role;
import com.yhsmy.entity.vo.sys.User;

import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/27 13:33
 **/
public interface RoleServiceI {

    /**
     * 获取角色列表
     *
     * @param params 查询条件
     * @return
     */
    public DataGrid getListData (QueryParams params);

    /**
     * 获取表单编辑时需要的参数
     *
     * @param id 角色ID
     * @return 参数集合
     */
    public Map<String, Object> getForm (String id, boolean isVeiw);

    /**
     * 保存或更新角色对象
     *
     * @param role 角色对象
     * @param user 当前操作的用户
     * @return 操作结果
     */
    public Json formSubmit (Role role, User user);

    /**
     * 删除角色
     *
     * @param id   角色ID
     * @param user 当前操作的用户
     * @return 操作结果
     */
    public Json delete (String id, User user);


}
