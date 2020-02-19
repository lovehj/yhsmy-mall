package com.yhsmy.web.activiti;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.Leave;
import com.yhsmy.service.act.LeaveServiceI;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @auth 李正义
 * @date 2019/12/20 20:05
 **/
@Api("请假接口")
@Controller
@Scope("request")
@RequestMapping("/leave")
public class LeaveController extends BaseController {

    @Autowired
    private LeaveServiceI leaveServiceI;

    @SysLog(content = "请假列表页面")
    @ApiOperation(value = "请假列表页面")
    @RequiresPermissions("leave:list")
    @GetMapping("list")
    public String leave () {
        return "activiti/leave/list";
    }

    @SysLog(content = "请假列表数据")
    @ApiOperation(value = "请假列表数据")
    @RequiresPermissions("leave:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (@RequestParam(defaultValue = "0") int state, QueryParams queryParams) {
        return leaveServiceI.getLeaveList (state, queryParams, ShiroUtil.getUser ());
    }

    @SysLog(content = "请假编辑页面")
    @ApiOperation(value = "请假编辑页面")
    @RequiresPermissions("leave:edit")
    @GetMapping(value = {"form", "view"})
    public String form (String id, @RequestParam(defaultValue = "false") boolean noCloseBtn, Model model, HttpServletRequest request) {
        String uri = request.getRequestURI ();
        model.addAttribute ("leaveView", (StringUtils.isNotBlank (id) && uri.indexOf ("view")>-1) ? true : false);
        model.addAllAttributes (leaveServiceI.getForm (id));
        model.addAttribute ("noCloseBtn", noCloseBtn);
        return "activiti/leave/form";
    }

    @SysLog(content = "请假编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "请假编辑")
    @RequiresPermissions("leave:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit (@Valid Leave leave, BindingResult result) {
        try {
            new BigDecimal (leave.getDays ());
        } catch (Exception e) {
            return Json.fail ("请假天数只能是数字!");
        }

        if(StringUtils.isEmpty (leave.getRemark ())) {
            leave.setRemark ("");
        }

        Json json = validResult (result);
        if (json.getStatus () != Json.SUC_CODE) {
            return json;
        }
        return leaveServiceI.formSubmit (leave, ShiroUtil.getUser ());
    }

    @SysLog(content = "请假删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @ApiOperation(value = "请假删除")
    @RequiresPermissions("leave:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return leaveServiceI.delete (id, ShiroUtil.getUser ());
    }

//    @ApiOperation(value = "查看审批详情")
//    @GetMapping("auditDetail")
//    public String auditDetail(String processInstanceId, Model model) {
//        model.addAttribute ("leaveAuditDetail", FastJsonUtil.listToJSONArrayString (leaveServiceI.getLeaveAuditRecord(processInstanceId)));
//        return "activiti/leave/auditDetail";
//    }
}
