package com.yhsmy.web;

import com.alibaba.fastjson.JSON;
import com.yhsmy.entity.Json;
import com.yhsmy.enums.ErrorStatusEnum;
import com.yhsmy.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @auth 李正义
 * @date 2019/11/11 22:51
 **/
@Slf4j
public abstract class BaseController {

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    protected String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        String message = ErrorStatusEnum.FORBIDDEN.getValue ();
        if(RequestUtil.isAjaxRequest (request)) {
            return JSON.toJSONString (Json.warp (ErrorStatusEnum.FORBIDDEN.getKey (), message));
        }
        try{
            message = URLEncoder.encode (message, "UTF-8");
        }catch (Exception e){
            log.error ("BaseController:"+e.getMessage ());
        }
        return "redirect:/error/403?message=" + message;
    }

//    protected Json catchJsonResult(Json jsonResult) {
//        try{
//            return jsonResult;
//        }catch(Exception e) {
//            if(StringUtils.isNotEmpty (e.getMessage ())) {
//                return Json.fail (e.getMessage ());
//            }
//            return Json.fail ();
//        }
//    }

    protected Json validResult(BindingResult result) {
        if(result.hasErrors ()) {
            StringBuilder builder = new StringBuilder ();
            for(FieldError fieldError : result.getFieldErrors ()) {
                builder.append (fieldError.getDefaultMessage ()).append ("\n");
            }
            return Json.fail (builder.toString ());
        }
        return Json.ok ();
    }

}
