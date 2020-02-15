package com.yhsmy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @auth 李正义
 * @date 2019/12/14 12:03
 **/
public class SpringUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext () {
        return applicationContext;
    }

    /**
     * 根据名称获取Bean对象
     *
     * @param name
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean (String name) {
        return (T) getApplicationContext ().getBean (name);
    }

    public static <T> T getBean(Class<T> tClass) {
        return getApplicationContext ().getBean (tClass);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
