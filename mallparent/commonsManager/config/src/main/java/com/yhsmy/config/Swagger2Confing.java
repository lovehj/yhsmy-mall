package com.yhsmy.config;

import com.yhsmy.IConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2接口文档配置
 *
 * @auth 李正义
 * @date 2019/12/1 11:43
 **/
@Configuration
@EnableSwagger2
public class Swagger2Confing {

    @Bean
    public Docket createRestApi(){
        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo())
                .select ()
                .apis (RequestHandlerSelectors.basePackage ("com.yhsmy.web"))
                .paths (PathSelectors.any ())
                .build ();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder ()
                .title ("在线接口文档")
                .description ("基于rest风格的API")
                .termsOfServiceUrl (IConstant.MALL_URL)
                .contact (new Contact ("李正义", IConstant.MALL_URL, "lishijiazhu_4@163.com"))
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build ();
    }

}
