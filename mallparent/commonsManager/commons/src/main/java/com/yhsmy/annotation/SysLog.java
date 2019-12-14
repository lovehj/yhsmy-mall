package com.yhsmy.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @auth 李正义
 * @date 2019/12/10 10:35
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface SysLog {

    /**
     * 日志类型枚举
     */
    public enum LOG_TYPE_ENUM {ADD, UPDATE, DELETE, SELECT, ATHOR};

    /**
     * 日志描述内容
     * @return
     */
    String content() default "" ;

    /**
     * 操作类型，对应CURD
     * @return
     */
    LOG_TYPE_ENUM type() default LOG_TYPE_ENUM.ATHOR;


}
