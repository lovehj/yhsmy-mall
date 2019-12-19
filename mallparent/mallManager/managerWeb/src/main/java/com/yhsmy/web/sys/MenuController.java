package com.yhsmy.web.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.MenuTypeEnum;
import com.yhsmy.service.sys.MenuServiceI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/24 9:30
 **/
@Controller
@Scope("request")
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuServiceI menuServiceI;

    @SysLog(content = "菜单列表页面")
    @RequiresPermissions("menu:list")
    @GetMapping("list")
    public String list (Model model) {
        model.addAttribute ("menus", FastJsonUtil.listToJSONArrayString (menuServiceI.getMenuList (-1)));
        return "sys/menu/list";
    }

    @SysLog(content = "菜单编辑页面", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions(value = {"menu:list", "menu:edit"})
    @GetMapping("form")
    public String form (String id, Model model) {
        String menuJson = FastJsonUtil.listToJSONArrayString (menuServiceI.getMenuList (MenuTypeEnum.OPERA.getKey ()));
        model.addAttribute ("menu", menuServiceI.getMenuById (id));
        model.addAttribute ("menus", menuJson);
        return "sys/menu/form";
    }


    @SysLog(content = "菜单编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("menu:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json edit (@Valid Menu menu, BindingResult result) {
        User user = ShiroUtil.getUser ();
        Map<String, Object> errorMap = new LinkedHashMap<> (1);
        if (result.hasErrors ()) {
            for (FieldError fieldError : result.getFieldErrors ()) {
                errorMap.put (fieldError.getField (), fieldError.getDefaultMessage ());
            }
        }
        // 额外的javaBean校验
        if (menu.getCtype () == 0) {
            // 菜单类型为目录时，不具有菜单地址、权限地址、上级目录,但是具有菜单图标
            if (StringUtils.isNotEmpty (menu.getUrl ())) {
                errorMap.put ("url", "菜单类型为目录时，不应该有请求地址!");
            }

            if (StringUtils.isNotEmpty (menu.getPermission ())) {
                errorMap.put ("permission", "菜单类型为目录时，不应该有请求权限！");
            }

            if (StringUtils.isNotEmpty (menu.getPid ())) {
                errorMap.put ("pid", "菜单类型为目录时，不应该有上级目录！");
            }

            if (StringUtils.isEmpty (menu.getIcon ())) {
                errorMap.put ("icon", "菜单类型为目录是，需要选择图标!");
            }
        } else {
            if (menu.getCtype () == 1) {
                if (StringUtils.isEmpty (menu.getIcon ())) {
                    errorMap.put ("icon", "菜单类型为菜单时，需要选择图标!");
                }
            }

            if (StringUtils.isEmpty (menu.getName ())) {
                errorMap.put ("name", "菜单名称必填!");
            }

            if (StringUtils.isEmpty (menu.getUrl ())) {
                errorMap.put ("url", "菜单地址必填!");
            }

            if (StringUtils.isEmpty (menu.getPermission ())) {
                errorMap.put ("permission", "菜单权限地址必填！");
            }

            if (StringUtils.isEmpty (menu.getPid ())) {
                errorMap.put ("pid", "上级菜单必选!");
            }
        }

        if (errorMap.size () > 0) {
            return Json.fail (errorMap);
        }
        return menuServiceI.formSubmit (menu, user);
    }

    @SysLog(content = "菜单删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @RequiresPermissions("menu:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        User user = ShiroUtil.getUser ();
        return menuServiceI.delete (id, user);
    }

}
