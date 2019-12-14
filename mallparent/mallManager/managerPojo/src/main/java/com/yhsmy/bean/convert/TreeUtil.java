package com.yhsmy.bean.convert;

import com.yhsmy.entity.Tree;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.enums.MenuTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/29 14:28
 **/
public class TreeUtil {

    /**
     * 把菜单的集合转换为tree的集合
     *
     * @param menuList   菜单集合
     * @param checkedMap 已选中的节点
     * @return
     */
    public static List<Tree> menuToTree (List<Menu> menuList, Map<String, String> checkedMap) {
        List<Tree> treeList = new ArrayList<> (menuList.size ());
        for (Menu m : menuList) {
            treeList.add (TreeUtil.copyMenuToTree (m, checkedMap));
        }
        return treeList;
    }

    /**
     * 将菜单的数据设置到tree中
     *
     * @param menu
     * @param checkedMap
     * @return
     */
    private static Tree copyMenuToTree (Menu menu, Map<String, String> checkedMap) {
        boolean checked = false;
        if (checkedMap != null && checkedMap.get (menu.getId ()) != null) {
            checked = true;
        }
        Tree tree = new Tree (menu.getCtype () + 1, menu.getId (), menu.getName (), menu.getPid (), true, checked);
        int size = 0;
        if (menu.getChildren () != null) {
            size = menu.getChildren ().size ();
        }
        if (size > 0 && menu.getCtype () != MenuTypeEnum.OPERA.getKey ()) {
            tree.setChildren (TreeUtil.menuToTree (menu.getChildren (), checkedMap));
        }
        return tree;
    }

}
