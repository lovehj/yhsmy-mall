package com.yhsmy.config.aspect;

import com.alibaba.fastjson.JSON;
import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.vo.sys.Log;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.sys.LogMapper;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.utils.RequestUtil;
import com.yhsmy.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 日志切面拦截
 *
 * @auth 李正义
 * @date 2019/12/10 10:51
 **/
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private LogMapper logMapper;

    /**
     * 定义日志切点
     */
    @Pointcut("@annotation(com.yhsmy.annotation.SysLog)")
    public void logPointCut () {

    }

    @After("logPointCut()")
    public void addLogPintCut (JoinPoint jp) {
        addLog (jp, "");
    }

    /**
     * 抛出异常的日志
     *
     * @param jp JoinPoint
     * @param e  异常信息
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void afterException (JoinPoint jp, Exception e) {
        String message = e.getMessage ();
        if (StringUtils.isBlank (message)) {
            if(e instanceof PageNotFoundException) {
                message = "页面未找到!";
            } else if(e instanceof ServiceException) {
                message = "业务逻辑处理异常!";
            } else {
                message = "未知的异常!";
            }
        }
        int maxLength = 255;
        if (message.length () > maxLength) {
            message = message.substring (0, maxLength - 1);
        }
        addLog (jp, message);
    }

    private void addLog (JoinPoint jp, String exceptionMsag) {
        // 获取当前注解的操作类型
        MethodSignature methodSignature = (MethodSignature) jp.getSignature ();
        Method method = methodSignature.getMethod ();

        Log log = new Log ();
        log.setLogId (UUIDUtil.generateUUID ());
        log.setCreateTime (LocalDateTime.now ());
        log.setState (NormalEnum.NORMAL.getKey ());
        log.setOperaType (method.getAnnotation (SysLog.class).type ().toString ());
        log.setRemark (method.getAnnotation (SysLog.class).content ());
        log.setExceptionMessage (StringUtils.isBlank (exceptionMsag) ? "" : exceptionMsag);
        log.setRequestMethod (jp.getSignature ().toString ());

        // 获取当前操作的用户
        try {
            log.setOperator (ShiroUtil.getUser ().getRealName ());
        } catch (Exception e) {
            log.setOperator ("未知的用户!");
        }

        // 获取requestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes ();

        // 设置请求信息
        if (requestAttributes != null) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ()).getRequest ();

            log.setFormIp (RequestUtil.getIp (request));
            log.setFormDevice (RequestUtil.getPlatform (request));
            String[] browsers = RequestUtil.getBroswers (request);
            log.setBrowser (browsers[0]);
            log.setBrowserVersion (browsers[1]);
        }

        Object[] paramArr = jp.getArgs ();
        StringBuilder paramBuilder = new StringBuilder ("");
        if (paramArr != null && paramArr.length > 0) {
            int paramLen = paramArr.length;
            for (int i = 0; i < paramLen; i++) {
                Object param = paramArr[i];
                if (param instanceof Model || param instanceof ModelMap) {
                    continue;
                }
                paramBuilder.append ("[参数" + (i + 1) + ":");
                try {
                    paramBuilder.append (JSON.toJSONString (param)).append ("]");
                } catch (Exception e) {
                    continue;
                }
            }
        }

        log.setRequestParams (paramBuilder.toString ());
        logMapper.addLog (log);
    }

}
