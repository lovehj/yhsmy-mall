package com.yhsmy.web.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Depart;
import com.yhsmy.service.sys.DepartServiceI;
import com.yhsmy.util.ShiroUtil;
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

/**
 * @auth 李正义
 * @date 2019/11/29 17:05
 **/
@Controller
@Scope("request")
@RequestMapping("/depart")
public class DepartController extends BaseController {

    @Autowired
    private DepartServiceI departServiceI;

    @SysLog(content = "部门列表页面")
    @RequiresPermissions("depart:list")
    @GetMapping("list")
    public String list(Model model) {
        String departJson = JSON.toJSONString (JSONArray.parseArray (JSON.toJSONString (departServiceI.getDepartList(true))));
        model.addAttribute ("departs", departJson);
        return "sys/depart/list";
    }

    @SysLog(content = "部门编辑页面", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("depart:edit")
    @GetMapping("form")
    public String form(String id,Model model) {
        model.addAllAttributes (departServiceI.getForm(id));
        return "sys/depart/form";
    }

    @SysLog(content = "部门编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @RequiresPermissions("depart:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit(@Valid Depart depart, BindingResult result) {
        Json res = validResult (result);
        if(res.getStatus () != Json.SUC_CODE) {
            return res;
        }
        return departServiceI.formSubmit(depart, ShiroUtil.getUser ());
    }

    @SysLog(content = "部门删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @RequiresPermissions("depart:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete(String id) {
        if(StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return departServiceI.delete(id,ShiroUtil.getUser ());
    }

}
