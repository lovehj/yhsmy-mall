package com.yhsmy.service.sys.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.bean.convert.TreeUtil;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.Role;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.sys.MenuMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.mapper.sys.RoleMapper;
import com.yhsmy.service.sys.MenuServiceI;
import com.yhsmy.service.sys.RoleServiceI;
import com.yhsmy.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @auth 李正义
 * @date 2019/11/27 13:34
 **/
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleServiceI {

    private static final String TABLE_ROLE = "smy_role";

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private MenuServiceI menuServiceI;


    @Override
    public DataGrid getListData (QueryParams params) {
        Page<Role> page = PageHelper.startPage (params.getPageNo (), params.getPageSize ());
        List<Role> roleList = roleMapper.findRoleList (params.getQueryBy (), params.getQueryText ());
        return new DataGrid (page.getTotal (), roleList);
    }

    @Override
    public Map<String, Object> getForm (String id, boolean isView) {
        Map<String, Object> map = new HashMap<> (1);
        Map<String, String> checkedMap = null;
        Role role = null;
        if (StringUtils.isNotBlank (id)) {
            role = roleMapper.findRoleById (id);
            if (role == null) {
                throw new PageNotFoundException ();
            }
        } else {
            role = new Role ();
            role.setRoleId ("");
            role.setRoleName ("");
            role.setRemark ("");
            role.setMenuIds ("");
        }
        List<Menu> menus = menuServiceI.getMenuList (-1);
        if (isView || StringUtils.isNotBlank (id)) {
            StringBuilder menuIds = new StringBuilder ();
            checkedMap = new HashMap<> (menus.size ());
            Map<String, Menu> menuMap = menuServiceI.getMenuIdByRoleId (id);
            for (Map.Entry<String, Menu> entry : menuMap.entrySet ()) {
                String key = entry.getKey ();
                checkedMap.put (key, key);
                menuIds.append (key).append (",");
            }
            role.setMenuIds (menuIds.toString ());
            if (role.getMenuIds ().endsWith (",")) {
                role.setMenuIds (role.getMenuIds ().substring (0, role.getMenuIds ().length () - 1));
            }
        }
        String treeJson = JSON.toJSONString (JSONArray.parseArray (JSON.toJSONString (TreeUtil.menuToTree (menus, checkedMap))));
        map.put ("role", role);
        map.put ("menus", treeJson);
        map.put ("isView", isView);
        return map;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (Role role, User user) {
        String cname = user.getRealName (),
                roleId = StringUtils.trim (role.getRoleId ());
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isEmpty (roleId)) {
            roleId = UUIDUtil.generateUUID ();
            role.setRoleId (roleId);
            role.setState (NormalEnum.NORMAL.getKey ());
            role.setCreator (cname);
            role.setModifyor (cname);
            role.setCreateTime (now);
            role.setModifyTime (now);
            roleMapper.addRole (role);
        } else {
            UpdateMap updateMap = new UpdateMap (TABLE_ROLE);
            updateMap.addField ("roleName", role.getRoleName ());
            updateMap.addField ("remark", role.getRemark ());
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("roleId", roleId);
            if (this.mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        }
        this.addOrRemoveRoleRelation (roleId, role.getMenuIds (), cname, 0);
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public Json delete (String id, User user) {
        String cname = user.getRealName ();
        UpdateMap delMap = new UpdateMap (TABLE_ROLE);
        delMap.addField ("state", NormalEnum.DELETE.getKey ());
        delMap.addField ("modifyor", cname);
        delMap.addField ("modifyTime", LocalDateTime.now ());
        delMap.addWhere ("roleId", id);
        if (this.mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }
        this.addOrRemoveRoleRelation (id, null, cname, 1);
        return Json.ok ();
    }

    /**
     * @param roleId    角色ID
     * @param menuIds   菜单ID，以","分隔
     * @param operator  操作者
     * @param operaFlag 0=编辑 1=删除(会解除与用户之间的关联关系)
     */
    private void addOrRemoveRoleRelation (String roleId, String menuIds, String operator, int operaFlag) {
        // 解除当前角色与菜单之间的关联关系
        UpdateMap delMap = new UpdateMap ("smy_role_menu");
        delMap.addWhere ("rm_roleId", roleId);
        this.mybatisMapper.delete (delMap);

        // 保存角色与菜单之间的关联关系
        if (StringUtils.isNotBlank (StringUtils.trim (menuIds))) {
            roleMapper.addRoleMenu (roleId, Arrays.asList (StringUtils.split (menuIds, ",")));
        }

        // 如果是删除,需要解除用户与角色之间的关系，并把用户设置为冻结状态
        if (operaFlag > 0) {
            UpdateMap updateMap = new UpdateMap ("smy_user");
            updateMap.addField ("roleId", "");
            updateMap.addField ("state", NormalEnum.FREEZE.getKey ());
            updateMap.addField ("modifyor", operator);
            updateMap.addField ("modifyTime", LocalDateTime.now ());
            updateMap.addWhere ("roleId", roleId);
            this.mybatisMapper.update (updateMap);
        }

    }
}
