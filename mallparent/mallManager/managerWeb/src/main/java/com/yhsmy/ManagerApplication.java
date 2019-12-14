package com.yhsmy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @auth 李正义
 * @date 2019/11/9 23:17
 **/
@SpringBootApplication(scanBasePackages = "com.yhsmy")
@EnableTransactionManagement
@EnableCaching
@MapperScan(value = {"com.yhsmy.mapper.*"})
public class ManagerApplication {

    public static void main (String[] args) {
        SpringApplication.run (ManagerApplication.class, args);
    }
}
