package com.yhsmy.web.activiti;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.config.rabbitmq.RabbitProducer;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.RabbitMqMessage;
import com.yhsmy.entity.vo.act.AuditRecord;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.service.act.WaitTaskServiceI;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 待办任务
 *
 * @auth 李正义
 * @date 2019/12/24 21:38
 **/
@Api("待办任务")
@Controller
@Scope("request")
@RequestMapping("/wait")
public class WaitTaskController extends BaseController {

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private WaitTaskServiceI waitTaskServiceI;

    @SysLog(content = "待办任务列表页面")
    @ApiOperation(value = "待办任务列表页面")
    @RequiresPermissions("wait:list")
    @GetMapping("list")
    public String list () {
        return "activiti/waitTask/list";
    }

    @SysLog(content = "待办任务列表数据")
    @ApiOperation(value = "待办任务列表数据")
    @RequiresPermissions("wait:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (QueryParams queryParams) {
        return waitTaskServiceI.getListDate (queryParams, ShiroUtil.getUser ());
    }

    @SysLog(content = "任务办理页面")
    @ApiOperation(value = "任务办理页面")
    @RequiresPermissions("wait:complete")
    @GetMapping("form/{taskId}/{processInstanceId}")
    public String form (@PathVariable String taskId, @PathVariable String processInstanceId, Model model) {
        model.addAllAttributes (waitTaskServiceI.getAgent (taskId, processInstanceId));
        model.addAttribute ("hasOperaBtn", true);
        return "activiti/waitTask/form";
    }

    @SysLog(content = "任务办理", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "任务办理")
    @RequiresPermissions("wait:complete")
    @PostMapping("complete")
    @ResponseBody
    public Json complete (AuditRecord auditRecord) {
        if (StringUtils.isBlank (auditRecord.getTaskId ())) {
            return Json.fail ("当前任务不存在!");
        }

        if (StringUtils.isBlank (auditRecord.getOpinion ())) {
            return Json.fail ("审批意见不能为空!");
        } else if (auditRecord.getOpinion ().length () > 200) {
            return Json.fail ("审批意见长度控制在200个汉字内!");
        }

        Json result = waitTaskServiceI.complete (auditRecord, ShiroUtil.getUser ());
        if (result.getStatus () == Json.SUC_CODE
                && result.getObj () instanceof RabbitMqMessage) {
            rabbitProducer.simpleProducer ((RabbitMqMessage) result.getObj ());
        }
        result.setObj (null);
        return result;
    }

    @SysLog(content = "启动流程审批", type = SysLog.LOG_TYPE_ENUM.ADD)
    @ApiOperation(value = "启动流程审批")
    @PostMapping("startTask")
    @ResponseBody
    public Json startTask (String businessId, String startKey, String stateFields) {
        if (StringUtils.isBlank (businessId) || StringUtils.isBlank (startKey)) {
            return Json.fail ();
        }
        if (StringUtils.isBlank (ApproveEnum.getValueByKey (startKey))) {
            return Json.fail ("流程启动的KEY不存在!");
        }
        return waitTaskServiceI.startTask (businessId, startKey, stateFields);
    }

    @SysLog(content = "获取审批详情/历史", type = SysLog.LOG_TYPE_ENUM.SELECT)
    @ApiOperation(value = "获取审批详情/历史")
    @GetMapping("auditDetail")
    public String auditDetail(String processInstanceId, Model model) {
        if(StringUtils.isEmpty (processInstanceId)) {
            throw new PageNotFoundException ();
        }
        model.addAttribute ("auditDetailList", FastJsonUtil.listToJSONArrayString (waitTaskServiceI.getAuditDetail(processInstanceId)));
        return "activiti/waitTask/auditDetail";
    }

    @SysLog(content = "已办理任务页面", type = SysLog.LOG_TYPE_ENUM.SELECT)
    @ApiOperation(value = "已办理任务页面")
    @GetMapping("finishedList")
    public String finishedList(Model model) {
        model.addAttribute ("approveList", ApproveEnum.getList ());
        return "activiti/waitTask/finishedList";
    }

    @SysLog(content = "已办理任务列表数据")
    @ApiOperation(value = "已办理任务列表数据")
    @GetMapping("finishedListData")
    @ResponseBody
    public DataGrid finishedListData(QueryParams queryParams) {
        return waitTaskServiceI.getFinishedListData(queryParams, ShiroUtil.getUser ());
    }

    @SysLog(content = "任务办理页面")
    @ApiOperation(value = "任务办理页面")
    @GetMapping("view/{startKey}/{processInstanceId}")
    public String view (@PathVariable String startKey, @PathVariable String processInstanceId, Model model) {
        String iframeUrl = waitTaskServiceI.getIframeUrl (startKey, processInstanceId);
        if(StringUtils.isBlank (iframeUrl)) {
            return "redirect:/error/404";
        }
        model.addAttribute ("iframeUrl", iframeUrl);
        model.addAttribute ("hasOperaBtn", false);
        model.addAttribute ("taskId", "");
        return "activiti/waitTask/form";
    }

}
