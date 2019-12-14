package com.yhsmy.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * freemarker模板配置类
 *
 * @auth 李正义
 * @date 2019/11/7 9:32
 **/
@Configuration
public class FreemarkerConfig {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void setSharedVariable () throws TemplateModelException {
        freeMarkerConfigurer.getConfiguration ()
                .setSharedVariable ("shiro", new ShiroTags ());
    }

}
