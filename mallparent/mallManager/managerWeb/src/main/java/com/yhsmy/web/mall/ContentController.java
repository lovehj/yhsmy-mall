package com.yhsmy.web.mall;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Content;
import com.yhsmy.entity.vo.mall.ContentCate;
import com.yhsmy.service.mall.ContentServiceI;
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

import javax.validation.Valid;

/**
 * @auth 李正义
 * @date 2020/1/5 21:08
 **/
@Api("内容分类及内容接口")
@Controller
@Scope("request")
@RequestMapping("/content")
public class ContentController extends BaseController {

    @Autowired
    private ContentServiceI contentServiceI;

    @SysLog(content = "内容分类列表页面")
    @ApiOperation(value = "内容分类列表页面")
    @RequiresPermissions("content:cateList")
    @GetMapping("cateList")
    public String cateList (QueryParams queryParams, Model model) {
        model.addAttribute ("contentCate", FastJsonUtil.listToJSONArrayString (contentServiceI.getContentCateList (queryParams)));
        return "mall/content/cateList";
    }

    @SysLog(content = "内容分类表单页面")
    @ApiOperation(value = "内容分类表单页面")
    @RequiresPermissions("content:cateEdit")
    @GetMapping("cateForm")
    public String cateForm (String cateId, Model model) {
        model.addAllAttributes (contentServiceI.getContentCateForm (cateId));
        return "mall/content/cateForm";
    }

    @SysLog(content = "内容分类编辑")
    @ApiOperation(value = "内容分类编辑")
    @RequiresPermissions("content:cateEdit")
    @PostMapping("cateFormSubmit")
    @ResponseBody
    public Json cateFormSubmit (@Valid ContentCate cate, BindingResult result) {
        Json resultJson = validResult (result);
        if (Json.SUC_CODE != resultJson.getStatus ()) {
            return resultJson;
        }
        return contentServiceI.contentCateFormSubmit (cate, ShiroUtil.getUser ());
    }

    @SysLog(content = "内容分类删除")
    @ApiOperation(value = "内容分类删除")
    @RequiresPermissions("content:cateDelete")
    @DeleteMapping("cateDelete")
    @ResponseBody
    public Json cateDelete (String cateId) {
        if (StringUtils.isBlank (cateId)) {
            return Json.fail ();
        }
        return contentServiceI.contentCateDelete (cateId, ShiroUtil.getUser ());
    }

    @SysLog(content = "内容列表页面")
    @ApiOperation(value = "内容列表页面")
    @RequiresPermissions("content:list")
    @GetMapping("list")
    public String list () {
        return "mall/content/list";
    }

    @SysLog(content = "内容列表数据")
    @ApiOperation(value = "内容列数据")
    @RequiresPermissions("content:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (@RequestParam(defaultValue = "0") int state, QueryParams queryParams) {
        return contentServiceI.getContentList (state,queryParams, ShiroUtil.getUser ());
    }

    @SysLog(content = "内容编辑页面")
    @ApiOperation(value = "内容编辑页面")
    @RequiresPermissions("content:edit")
    @GetMapping("form")
    public String form (String id, @RequestParam(defaultValue = "false") boolean contentView, @RequestParam(defaultValue = "false") boolean noCloseBtn, Model model) {
        model.addAllAttributes (contentServiceI.getContentForm (id));
        model.addAttribute ("contentView", contentView);
        model.addAttribute ("noCloseBtn", noCloseBtn);
        return "mall/content/form";
    }

    @SysLog(content = "内容详情页面")
    @ApiOperation(value = "内容详情页面")
    @GetMapping("view")
    public String view (String id, @RequestParam(defaultValue = "false") boolean noCloseBtn, Model model) {
        return this.form (id, true, noCloseBtn, model);
    }

    @SysLog(content = "内容编辑")
    @ApiOperation(value = "内容编辑")
    @RequiresPermissions("content:edit")
    @PostMapping("formSubmit")
    @ResponseBody
    public Json formSubmit (@Valid Content content, BindingResult result) {
        Json resultJson = validResult (result);
        if (Json.FAIL_CODE == resultJson.getStatus ()) {
            return resultJson;
        }
        return contentServiceI.contentFormSubmit (content, ShiroUtil.getUser ());
    }

    @SysLog(content = "内容删除")
    @ApiOperation(value = "内容删除")
    @RequiresPermissions("content:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return contentServiceI.contentDelete (id, ShiroUtil.getUser ());
    }

}
