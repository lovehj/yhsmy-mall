package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/27 13:48
 **/
public interface RoleMapper {

    /**
     * 保存角色
     *
     * @param role 角色对象
     */
    public void addRole (Role role);

    /**
     * 查询角色集合
     *
     * @param queryBy   0=忽略 1=按角色名查询
     * @param queryText
     * @return 角色集合
     */
    public List<Role> findRoleList (@Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * 根据ID获取角色对象
     *
     * @param roleId 角色ID
     * @return 角色对象
     */
    public Role findRoleById (@Param("roleId") String roleId);

    /**
     * @param roleId     角色id
     * @param menuIdList 菜单集合
     */
    public void addRoleMenu (@Param("roleId") String roleId, @Param("menuIdList") List<String> menuIdList);


}
