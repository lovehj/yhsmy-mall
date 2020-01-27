package com.yhsmy.web.sys;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Message;
import com.yhsmy.service.sys.MessageServiceI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @auth 李正义
 * @date 2019/12/26 18:33
 **/
@Api("消息接口")
@Controller
@Scope("request")
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageServiceI messageServiceI;

    @SysLog(content = "消息列表页面")
    @ApiOperation(value = "消息列表页面")
    @GetMapping("list")
    public String list () {
        return "sys/message/list";
    }

    @SysLog(content = "消息列表页面")
    @ApiOperation(value = "消息列表页面")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (@RequestParam(defaultValue = "-1") int posistion, @RequestParam(defaultValue = "-1") int flag, QueryParams queryParams) {
        return messageServiceI.getListData (posistion, flag, queryParams, ShiroUtil.getUser ());
    }

    /**
     * 读取或删除消息
     *
     * @param id    消息ID
     * @param ctype 0=读取消息 1=删除消息
     * @return
     */
    @SysLog(content = "消息列读取或删除")
    @ApiOperation(value = "消息读取或删除")
    @PostMapping("readOrDelete")
    @ResponseBody
    public Json readOrDelete (String id, @RequestParam(defaultValue = "0") int ctype) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        if (ctype < 0) {
            ctype = 0;
        }
        if (ctype > 1) {
            ctype = 1;
        }
        return messageServiceI.readOrDelete (id, ctype, ShiroUtil.getUser ());
    }

    @GetMapping(value = "pushNew", produces = "text/event-stream;charset=UTF-8") //
    @ResponseBody
    public String pushNew (@RequestParam(defaultValue = "0") int ctype) {
        if (ctype == 0) {
            // 推送消息文本给浏览器
            Message message = messageServiceI.getPushMessage (ShiroUtil.getUser ());
            if (message != null) {
                return "data:" + message.getTitle () + "\n\n" + "id:" + message.getId ();
            }
        } else {
            // 推送未消息总数给浏览器
            long count = messageServiceI.getListData (-1, -1, new QueryParams (), ShiroUtil.getUser ()).getCount ();
            if(count > 0) {
                return "data:" + count + "\n\n";
            }
        }
        return ":noData";
    }

}
