package com.yhsmy.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * druid数据源配置
 *
 * @auth 李正义
 * @date 2019/12/1 15:14
 **/
//@Configuration
public class DruidConfig {

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean ();
//        filterRegistrationBean.setFilter (new WebStatFilter ());
//        filterRegistrationBean.addUrlPatterns ("/*");
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*,*.html");
//        DelegatingFilterProxy proxy = new DelegatingFilterProxy ();
//        proxy.setTargetFilterLifecycle (true);
//        proxy.setTargetBeanName ("shiroFilter");
//        filterRegistrationBean.setFilter (proxy);
//        return filterRegistrationBean;
//    }
//
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean() {
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
//        servletRegistrationBean.setServlet(new StatViewServlet ());
//        servletRegistrationBean.addUrlMappings ("/druid/*");
//        Map<String, String> initParameters = new HashMap<String, String> ();
//        initParameters.put("resetEnable", "false");
//        initParameters.put("allow", "");
//        servletRegistrationBean.setInitParameters (initParameters);
//        return servletRegistrationBean;
//    }

}
