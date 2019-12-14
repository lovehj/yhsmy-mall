package com.yhsmy.web.sys;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.service.sys.LogServiceI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志管理页面
 *
 * @auth 李正义
 * @date 2019/12/10 13:25
 **/
@Controller
@Scope("request")
@RequestMapping("/log")
public class LogController extends BaseController {

    @Autowired
    private LogServiceI logServiceI;

    @ApiOperation(value = "日志列表页面")
    @RequiresPermissions("log:list")
    @GetMapping("list")
    public String list() {
        return "sys/log/list";
    }

    @ApiOperation(value = "日志列表数据接口", notes = "返回JSON格式的数据")
    @RequiresPermissions("log:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData(QueryParams params) {
        return logServiceI.listDate (params);
    }


    @ApiOperation(value = "日志删除")
    @RequiresPermissions("log:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete(String id) {
        if(StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return logServiceI.delete (id, ShiroUtil.getUser ());
    }
}
