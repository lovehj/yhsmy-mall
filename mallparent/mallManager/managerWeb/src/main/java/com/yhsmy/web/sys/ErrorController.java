package com.yhsmy.web.sys;

import com.yhsmy.web.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @auth 李正义
 * @date 2019/11/24 10:32
 **/
@Controller
@Scope("request")
@RequestMapping("/error")
public class ErrorController extends BaseController {

    @GetMapping("404")
    public String pageNotFund() {
        return "/error/404";
    }

    @GetMapping("403")
    public String notFund() {
        return "/error/403";
    }


}
