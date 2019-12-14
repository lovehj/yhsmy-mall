package com.yhsmy.web.sys;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.service.sys.UserServcieI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/9 23:22
 **/
@Api("用户接口")
@Controller
@Scope("request")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserServcieI userServcieI;

    @SysLog(content = "用户列表页面")
    @ApiOperation(value = "用户列表页面")
    @RequiresPermissions("user:list")
    @GetMapping("list")
    public String list () {
        return "sys/user/list";
    }

    @SysLog(content = "用户列表数据", type = SysLog.LOG_TYPE_ENUM.SELECT)
    @ApiOperation(value = "用户列表数据接口", notes = "返回JSON格式的数据")
    @RequiresPermissions("user:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (@RequestParam(defaultValue = "0") int state, QueryParams queryParams) {
        return userServcieI.getListData (state, queryParams);
    }


    @SysLog(content = "用户编辑页面", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("user:edit")
    @GetMapping(value = {"form", "view"})
    public String form (String id, Model model, HttpServletRequest request) {
        String uri = request.getRequestURI ();
        model.addAllAttributes (userServcieI.getForm (id, true));
        model.addAttribute ("userView", StringUtils.isNotBlank (id) && uri.indexOf ("view") > -1 ? true : false);
        return "sys/user/form";
    }


    @SysLog(content = "用户编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "用户表单提交")
    @RequiresPermissions("user:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit (@Valid User editUser, BindingResult result) {
        Json res = validResult (result);
        if (res.getStatus () != Json.SUC_CODE) {
            return res;
        }
        return userServcieI.formSubmit (editUser, ShiroUtil.getUser ());
    }

    @SysLog(content = "用户删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @ApiOperation(value = "用户删除")
    @RequiresPermissions("user:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return userServcieI.delete (id, ShiroUtil.getUser ());
    }

    @SysLog(content = "更改密码页面", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "更改密码页面")
    @RequiresPermissions("user:editPasswd")
    @GetMapping("editPasswdForm")
    public String editPasswdForm (String id, Model model) {
        model.addAllAttributes (userServcieI.getForm (id, false));
        return "sys/user/editPasswd";
    }

    @SysLog(content = "更改密码", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "更改密码")
    @RequiresPermissions("user:editPasswd")
    @PostMapping("editPasswd")
    @ResponseBody
    public Json editPasswd (String id, String originalPasswd, String newPasswd, String confirmPsswd) {
        Map<String, Object> valid = new HashMap<> (1);
        if (StringUtils.isBlank (id)) {
            valid.put ("id", "请求参数错误!");
        }

        if (StringUtils.isEmpty (originalPasswd)) {
            valid.put ("originalPasswd", "原始密码不能为空!");
        }

        if (StringUtils.isEmpty (newPasswd)) {
            valid.put ("newPasswd", "新密码不能为空!");
        }

        if (StringUtils.isEmpty (confirmPsswd)) {
            valid.put ("confirmPsswd", "确认新密码不能为空!");
        }

        if (StringUtils.isNotBlank (newPasswd) && StringUtils.isNotBlank (confirmPsswd)) {
            if (!newPasswd.equals (confirmPsswd)) {
                valid.put ("newPasswd", "新密码和确认的新密码不一致!");
            }
        }

        if (valid.size () > 0) {
            return Json.fail (valid);
        }

        return userServcieI.updatePasswd (id, originalPasswd,newPasswd, ShiroUtil.getUser ());
    }


    @SysLog(content = "初始化密码", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "初始化密码")
    @RequiresPermissions("user:initPasswd")
    @PostMapping("initPasswd")
    @ResponseBody
    public Json initPasswd (String id, String newPasswd) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return userServcieI.updatePasswd (id,"", newPasswd, ShiroUtil.getUser ());
    }

    @SysLog(content = "启用/禁用用户", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "启用/禁用用户")
    @RequiresPermissions("user:status")
    @PostMapping("status")
    @ResponseBody
    public Json status (String id) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return userServcieI.updateStatus (id, ShiroUtil.getUser ());
    }


}
