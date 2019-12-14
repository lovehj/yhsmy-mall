package com.yhsmy.service.sys.impl;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.MenuTypeEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.mapper.sys.MenuMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.sys.MenuServiceI;
import com.yhsmy.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @auth 李正义
 * @date 2019/11/18 18:11
 **/
@Service
@Transactional(readOnly = true)
@Slf4j
public class MenuServiceImpl implements MenuServiceI {

    private static final String TABLE_MENU = "smy_menu";

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    @Transactional(readOnly = false)
    public void addMenu (Menu menu) {
        menu.setId (UUIDUtil.generateUUID ());
        menu.setCreateTime (LocalDateTime.now ());
        menu.setModifyTime (menu.getCreateTime ());
        menuMapper.addMenu (menu);
    }

    @Override
    public List<Menu> getMenuList (int exculdeMenuType) {
        List<Menu> menuList = menuMapper.findMenuList (null);
        if (menuList == null || menuList.size () <= 0) return null;
        return this.combRelation (menuList, exculdeMenuType);
    }

    @Override
    public List<Menu> getMenuListByRoleId (String roleId, int exculdeMenuType, boolean combRelation) {
        List<Menu> menuList = menuMapper.findMenuListByRoleId (roleId);
        if (menuList == null || menuList.size () <= 0) return null;
        if (combRelation) {
            return this.combRelation (menuList, exculdeMenuType);
        }
        return menuList;
    }

    @Override
    public Menu getMenuById (String id) {
        Menu menu = null;
        if (StringUtils.isNotEmpty (id)) {
            menu = menuMapper.findMenuById (id);
            if (menu == null) {
                throw new PageNotFoundException ();
            }
            if (StringUtils.isNotEmpty (StringUtils.trim (menu.getPid ()))) {
                Menu pMenu = menuMapper.findMenuById (menu.getPid ());
                if (pMenu != null) {
                    menu.setParentName (pMenu.getName ());
                } else {
                    throw new PageNotFoundException ("父级菜单未找到!");
                }
            }
        } else {
            menu = new Menu ();
        }
        return menu;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (Menu menu, User user) {
        LocalDateTime now = LocalDateTime.now ();
        String cname = user.getRealName ();
        if (StringUtils.isNotEmpty (menu.getIcon ()) &&
                !menu.getIcon ().startsWith ("&")) {
            menu.setIcon ("&" + menu.getIcon ());
        }
        if (StringUtils.isEmpty (menu.getId ())) {
            // 保存
            menu.setId (UUIDUtil.generateUUID ());
            menu.setOrderNum (this.getOrderNum (menu.getCtype (), menu.getPid ()));
            menu.setCreator (cname);
            menu.setModifyor (cname);
            menu.setCreateTime (now);
            menu.setModifyTime (now);
            menu.setState (NormalEnum.NORMAL.getKey ());
            menuMapper.addMenu (menu);
        } else {
            // 更新
            UpdateMap updateMap = new UpdateMap (TABLE_MENU);
            updateMap.addField ("name", menu.getName ());
            updateMap.addField ("url", menu.getUrl ());
            updateMap.addField ("icon", menu.getIcon ());
            updateMap.addField ("permission", menu.getPermission ());
            updateMap.addField ("ctype", menu.getCtype ());
            updateMap.addField ("pid", menu.getPid ());
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("id", menu.getId ());
            if (this.mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ("更新失败!");
            }
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public Json delete (String id, User user) {
        // 检查当前id下是否有未删除的子菜单
        List<Menu> childs = this.menuMapper.findMenuList (id);
        if (childs.size () > 0) {
            return Json.fail ("当前菜单下有未删除的子菜单，该菜单不允许删除!");
        }
        String cname = user.getRealName ();
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap updateMap = new UpdateMap (TABLE_MENU);
        updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        updateMap.addField ("modifyor", cname);
        updateMap.addField ("modifyTime", now);
        updateMap.addWhere ("id", id);
        if (this.mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        // 删除角色与菜单的关联关系
        updateMap = new UpdateMap ("smy_role_menu");
        updateMap.addWhere ("rm_menuId", id);
        this.mybatisMapper.delete (updateMap);
        return Json.ok ();
    }

    @Override
    public Map<String, Menu> getMenuIdByRoleId (String roleId) {
        Map<String, Menu> menuIdMap = new HashMap<> ();
        List<Menu> menuList = this.menuMapper.findMenuListByRoleId (roleId);
        for(Menu m : menuList) {
            menuIdMap.put (m.getId (),m );
        }
        return menuIdMap;
    }


    /**
     * @param menus           菜单集合对象
     * @param exculdeMenuType 排除某个菜单的类型 -1=忽略 0=目录 1=菜单 2=功能
     * @return 菜单集合，组合好父子关系
     */
    private List<Menu> combRelation (List<Menu> menus, int exculdeMenuType) {
        if (menus == null || menus.size () <= 0) {
            return null;
        }

        int dir = MenuTypeEnum.DIR.getKey (),
                menu = MenuTypeEnum.MENU.getKey (),
                opera = MenuTypeEnum.OPERA.getKey ();
        List<Menu> dirList = menus.stream ().filter (m -> m.getCtype () == dir).collect (Collectors.toList ()),
                menuList = menus.stream ().filter (m -> m.getCtype () == menu).collect (Collectors.toList ()),
                operaList = menus.stream ().filter (m -> m.getCtype () == opera).collect (Collectors.toList ());

        if (dir == exculdeMenuType) {
            // 如果排除的是根目录，则返回菜单目录及菜单下的功能目录
            return this.combMenuRelation (menuList, operaList);
        } else if (menu == exculdeMenuType) {
            // 如果排除的是菜单，则只返回目录类型
            return dirList;
        } else if (opera == exculdeMenuType) {
            // 如果排除的是功能，则返回目录及目录下的菜单
            return this.combMenuRelation (dirList, menuList);
        } else {
            // 如果都不排除，则返回目录及目录下的菜单及菜单下的功能
            return this.combParentAndSonMenuRelation (menus);
        }
    }

    private List<Menu> combMenuRelation (List<Menu> parentMenuList, List<Menu> childMenuList) {
        for (Menu parent : parentMenuList) {
            parent.setChildren (childMenuList.stream ()
                    .filter (child -> parent.getId ().equalsIgnoreCase (child.getPid ()))
                    .collect (Collectors.toList ()));
        }
        return parentMenuList;
    }

    private List<Menu> combParentAndSonMenuRelation (List<Menu> menus) {
        List<Menu> rootList = menus.stream ()
                .filter (m -> m.getCtype () == MenuTypeEnum.DIR.getKey ())
                .map (m -> this.combTree (m, menus))
                .collect (Collectors.toList ());
        return rootList;
    }

    private Menu combTree (Menu parent, List<Menu> menus) {
        if (parent.getCtype () == MenuTypeEnum.OPERA.getKey ()) {
            return parent;
        }

        for (Menu m : menus) {
            if (m.getChildren () == null) {
                m.setChildren (new ArrayList<> (1));
            }

            if (StringUtils.trim (m.getPid ()).equalsIgnoreCase (StringUtils.trim (parent.getId ()))
                    && m.getCtype () != parent.getCtype ()) {
                parent.getChildren ().add (m);
                combTree (m, menus);
            }
        }
        return parent;
    }

    private int getOrderNum (int ctype, String pid) {
        int dir = MenuTypeEnum.DIR.getKey (),
                menu = MenuTypeEnum.MENU.getKey (),
                opera = MenuTypeEnum.OPERA.getKey ();
        if (ctype < dir) {
            ctype = dir;
            pid = null;
        }

        if (ctype > opera) {
            ctype = opera;
        }
        int orderNum = menuMapper.findOrderNumByCtype (ctype, pid);
        if (orderNum <= 0) {
            if (ctype == dir) {
                orderNum = 1000;
            } else {
                Menu m = menuMapper.findMenuById (pid);
                if (m == null) {
                    orderNum = new Random ().nextInt (999);
                } else {
                    orderNum = m.getOrderNum () + (ctype == menu ? 100 : 1);
                }
            }
        } else if (ctype == dir) { //目录+1000
            orderNum += 1000;
        } else if (ctype == menu) { //菜单+100
            orderNum += 100;
        } else if (ctype == opera) { //按钮+1
            orderNum++;
        }
        return orderNum;
    }
}


