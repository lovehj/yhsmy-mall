package com.yhsmy.web.sys;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Role;
import com.yhsmy.service.sys.RoleServiceI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/27 12:03
 **/
@Api("角色接口")
@Controller
@Scope("request")
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleServiceI roleServiceI;

    @SysLog(content = "角色列表页面")
    @ApiOperation(value = "角色列表页面")
    @RequiresPermissions("role:list")
    @GetMapping("list")
    public String list () {
        return "sys/role/list";
    }

    @SysLog(content = "角色列表数据", type = SysLog.LOG_TYPE_ENUM.SELECT)
    @ApiOperation(value = "角色列表数据", notes = "数据以JSON格式返回")
    @RequiresPermissions("role:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (QueryParams params) {
        return roleServiceI.getListData (params);
    }

    @ApiOperation(value = "角色列表数据", notes = "数据以JSON格式返回")
    @GetMapping("listRole")
    @ResponseBody
    public String listRole(QueryParams params) {
        DataGrid dataGrid = roleServiceI.getListData (params);
        List<Role> roleList = null;
        try{
            if(dataGrid.getData ().size () >0) {
                roleList = (List<Role>) dataGrid.getData ();
            }
        }catch (Exception e) {

        }

        if(roleList == null) {
            roleList = new ArrayList<> (1);
        }
        return FastJsonUtil.listToJSONArrayString (roleList);
    }

    @SysLog(content = "角色编辑页面", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("role:edit")
    @GetMapping(value = {"form", "view"})
    public String form (String id, Model model, HttpServletRequest request) {
        boolean isView = request.getRequestURI ().indexOf ("view") > -1;
        if (isView && StringUtils.isEmpty (id)) {
            return "/error/404";
        }
        model.addAllAttributes (roleServiceI.getForm (id, isView));
        return "sys/role/form";
    }

    @SysLog(content = "角色编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("role:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit (@Valid Role role, BindingResult result) {
        Json res = validResult (result);
        if (res.getStatus () != Json.SUC_CODE) {
            return res;
        }
        if (StringUtils.isEmpty (role.getMenuIds ())) {
            return Json.fail ("请选择角色相关的权限");
        }
        return roleServiceI.formSubmit (role, ShiroUtil.getUser ());
    }

    @SysLog(content = "角色删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @RequiresPermissions("role:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return roleServiceI.delete (id, ShiroUtil.getUser ());
    }
}
