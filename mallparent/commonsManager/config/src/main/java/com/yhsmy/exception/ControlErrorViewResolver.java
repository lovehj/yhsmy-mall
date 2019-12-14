package com.yhsmy.exception;

import com.yhsmy.enums.ErrorStatusEnum;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/8 9:58
 **/
@ControllerAdvice
public class ControlErrorViewResolver implements ErrorViewResolver {

    static final String PAGE_500 = "/error/500";
    static final String PAGE_404 = "/error/404";
    static final String PAGE_403 = "/error/403";
    static final String OTHER_ERROR = "/error/error";

    @Override
    public ModelAndView resolveErrorView (HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        boolean isServerError = status.is5xxServerError ();
        ModelAndView mv = new ModelAndView ();
        mv.addObject ("message", model.get ("message"));
        int state = status.value ();
        if(state == ErrorStatusEnum.PAGE_NOT_FOUND.getKey ()) {
            mv.setViewName (PAGE_404);
        } else if(state == ErrorStatusEnum.FORBIDDEN.getKey ()) {
            mv.setViewName (PAGE_403);
        } else if(state == ErrorStatusEnum.INTERNAL.getKey ()) {
            mv.setViewName (PAGE_500);
        } else {
            mv.addObject ("status", state);
            mv.setViewName (OTHER_ERROR);
        }
        return mv;
    }
}
