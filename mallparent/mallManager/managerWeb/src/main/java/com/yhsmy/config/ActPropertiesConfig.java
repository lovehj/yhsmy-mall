package com.yhsmy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @auth 李正义
 * @date 2019/12/14 13:25
 **/
@Configuration
@PropertySource("classpath:properties\\activiti.properties")
public class ActPropertiesConfig {

    @Value("${modelId}")
    private int modelId;

    public int getModelId () {
        return modelId;
    }

    public void setModelId (int modelId) {
        this.modelId = modelId;
    }

    @Bean
    public ActPropertiesConfig getActPropertiesConfig () {
        return new ActPropertiesConfig ();
    }
}
