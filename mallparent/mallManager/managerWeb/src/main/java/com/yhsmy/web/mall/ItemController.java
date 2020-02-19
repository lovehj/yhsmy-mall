package com.yhsmy.web.mall;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Item;
import com.yhsmy.service.mall.ItemServiceI;
import com.yhsmy.util.ShiroUtil;
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
 * @date 2019/12/18 11:28
 **/
@Api("商品接口")
@Controller
@Scope("request")
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemServiceI itemServiceI;

    @SysLog(content = "商品列表页面")
    @ApiOperation(value = "商品列表页面")
    @RequiresPermissions("item:list")
    @GetMapping("list")
    public String list () {
        return "mall/item/list";
    }

    @SysLog(content = "商品列表页面")
    @ApiOperation(value = "商品列表页面")
    @RequiresPermissions("item:list")
    @GetMapping("listData")
    @ResponseBody
    public DataGrid listData (@RequestParam(defaultValue = "0") int state, QueryParams queryParams) {
        return itemServiceI.getListData (state, queryParams);
    }


    @SysLog(content = "商品编辑页面")
    @ApiOperation(value = "商品编辑页面")
    @RequiresPermissions("item:edit")
    @GetMapping("form")
    public String form (String id, @RequestParam(defaultValue = "false") boolean noCloseBtn, Model model, HttpServletRequest request) {
        return this.view (id, noCloseBtn, model, request);
    }

    /**
     * 商品查询的时候不需要权限
     *
     * @param id
     * @param noCloseBtn 没有关闭按钮
     * @param model
     * @param request
     * @return
     */
    @ApiOperation(value = "商品查阅页面")
    @GetMapping("view")
    public String view (String id, @RequestParam(defaultValue = "false") boolean noCloseBtn, Model model, HttpServletRequest request) {
        String uri = request.getRequestURI ();
        model.addAllAttributes (itemServiceI.getForm (id));
        model.addAttribute ("itemView", (StringUtils.isNotBlank (id) && uri.indexOf ("view") > -1) ? true : false);
        model.addAttribute ("noCloseBtn", noCloseBtn);
        return "mall/item/form";
    }

    @SysLog(content = "商品编辑")
    @ApiOperation(value = "商品编辑")
    @RequiresPermissions("item:edit")
    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit (@Valid Item item, BindingResult result) {
        Json json = validResult (result);
        if (json.getStatus () != Json.SUC_CODE) {
            return json;
        }
        BigDecimal price = new BigDecimal (item.getPrice ()), //原价
                disPrice = new BigDecimal (item.getDisPrice ()); // 折扣价
        if (disPrice.compareTo (price) > 0) {
            return Json.fail ("商品的折扣价不能大于商品原价!");
        }
        return itemServiceI.formSubmit (item, ShiroUtil.getUser ());
    }

    @SysLog(content = "商品删除")
    @ApiOperation(value = "商品删除")
    @RequiresPermissions("item:delete")
    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (@RequestParam(defaultValue = "0") int ctype, String id) {
        if (StringUtils.isBlank (id)) {
            return Json.fail ();
        }
        return itemServiceI.delete (ctype, id, ShiroUtil.getUser ());
    }

}
