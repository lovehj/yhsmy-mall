package com.yhsmy.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auth 李正义
 * @date 2019/11/8 10:29
 **/
@Slf4j
public class CustomException implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException (HttpServletRequest httpServletRequest,
                                          HttpServletResponse httpServletResponse,
                                          Object o, Exception e) {
        ModelAndView mv = new ModelAndView ("/error/error");//shiro无权限
        if(e instanceof UnauthorizedException) { //
            mv.setViewName ("/login");
            return mv;
        }

        Exception exception = null;
        if(e instanceof  ServiceException) {
            exception = (ServiceException) e;
        } else if(e instanceof PageNotFoundException) {
            exception = (PageNotFoundException)e;
            mv.setViewName ("/error/404");
        }  else {
            exception = new Exception ("未知错误");
        }

        mv.addObject ("message", exception.getMessage ());
        return mv;
    }
}
