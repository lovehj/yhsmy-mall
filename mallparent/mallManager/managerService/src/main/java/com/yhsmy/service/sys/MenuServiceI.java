package com.yhsmy.service.sys;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/18 18:08
 **/
public interface MenuServiceI {

    /**
     * 添加菜单
     *
     * @param menu 菜单对象
     */
    public void addMenu (Menu menu);

    /**
     * 获取所有的菜单,组合好父子关系
     *
     * @param exculdeMenuType 排除一菜单类型,类型字段见MenuTypeEnum枚举，-1=忽略
     * @return
     */
    public List<Menu> getMenuList (int exculdeMenuType);

    /**
     * 根据角色ID获取菜单对象
     *
     * @param roleId          角色ID
     * @param exculdeMenuType 排除一菜单类型,类型字段见MenuTypeEnum枚举，-1=忽略
     * @param combRelation    true = 需要组合父子关系 false=不组合父子关系
     * @return 菜单对象集合
     */
    public List<Menu> getMenuListByRoleId (String roleId, int exculdeMenuType, boolean combRelation);

    /**
     * 根据ID获取菜单对象
     *
     * @param id 菜单ID
     * @return 菜单
     */
    public Menu getMenuById (String id);

    /**
     * 保存菜单对象
     *
     * @param menu 菜单对象
     * @param user 当前操作的用户
     */
    public Json formSubmit (Menu menu, User user);

    /**
     * 删除菜单
     *
     * @param id   菜单ID
     * @param user 当前操作的用户
     * @return
     */
    public Json delete (String id, User user);

    /**
     * 根据角色ID获取相关的菜单ID
     * @param roleId 角色ID
     * @return
     */
    public Map<String, Menu> getMenuIdByRoleId(String roleId);

}
