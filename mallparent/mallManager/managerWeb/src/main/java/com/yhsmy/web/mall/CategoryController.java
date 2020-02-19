package com.yhsmy.web.mall;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Category;
import com.yhsmy.service.mall.CategoryServiceI;
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
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/18 9:21
 **/

@Api("商品分类接口")
@Controller
@Scope("request")
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryServiceI categoryServiceI;

    @SysLog(content = "商品分类列表页面")
    @ApiOperation(value = "商品分类列表页面")
    @RequiresPermissions("category:list")
    @GetMapping("list")
    public String list(Model model){
        model.addAttribute ("categoryList",
                FastJsonUtil.listToJSONArrayString (categoryServiceI.getCategoryRelation(new QueryParams (3, ""))));
        return "mall/category/list";
    }

    @SysLog(content = "商品分类列表数据", type = SysLog.LOG_TYPE_ENUM.SELECT)
    @ApiOperation(value = "商品分类表单")
    @RequiresPermissions("category:edit")
    @GetMapping("form")
    public String form(String id, Model model) {
        model.addAllAttributes (categoryServiceI.getForm (id));
        return "mall/category/form";
    }

    @SysLog(content = "商品分类编辑", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiOperation(value = "商品分类表单提交")
    @RequiresPermissions("category:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit(@Valid Category category, BindingResult result) {
        Json json = validResult (result);
        if(json.getStatus () != Json.SUC_CODE) {
            return json;
        }
        if(StringUtils.isBlank (category.getCatePId ())) {
            category.setCatePId ("");
        }
        return categoryServiceI.formSubmit (category, ShiroUtil.getUser ());
    }

    @SysLog(content = "商品分类删除", type = SysLog.LOG_TYPE_ENUM.DELETE)
    @ApiOperation(value = "商品分类删除")
    @RequiresPermissions("category:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete(String id) {
        if(StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return categoryServiceI.delete (id, ShiroUtil.getUser ());
    }


}
