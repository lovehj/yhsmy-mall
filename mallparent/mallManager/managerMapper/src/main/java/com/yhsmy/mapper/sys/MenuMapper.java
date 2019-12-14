package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/18 17:50
 **/
public interface MenuMapper {

    /**
     * 添加菜单
     *
     * @param menu 菜单对象
     */
    public void addMenu (Menu menu);

    /**
     * 获取拥有的菜单
     *
     * @param pid 根据上级菜单查询子菜单,null=忽略
     * @return 菜单集合
     */
    public List<Menu> findMenuList (@Param("pid") String pid);

    /**
     * 根据角色ID获取菜单对象
     *
     * @param roleId 角色ID
     * @return 菜单对象集合
     */
    public List<Menu> findMenuListByRoleId (@Param("roleId") String roleId);

    /**
     * 根据菜单ID查询对象
     *
     * @param id 菜单ID
     * @return 菜单
     */
    public Menu findMenuById (@Param("id") String id);

    /**
     * @param ctype 菜单类型,参数值为menuTypeEnum
     * @param pid   当ctype不为目录时,需要pid参数
     * @return 表中最大的排序值
     */
    public int findOrderNumByCtype (@Param("ctype") int ctype, @Param("pid") String pid);

}
