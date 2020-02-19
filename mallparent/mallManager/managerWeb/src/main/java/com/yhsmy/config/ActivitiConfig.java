package com.yhsmy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * activiti的配置文件
 *
 * @auth 李正义
 * @date 2019/12/14 13:03
 **/
@Configuration
public class ActivitiConfig {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration (DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
        SpringProcessEngineConfiguration pec = new SpringProcessEngineConfiguration ();
        pec.setDataSource (dataSource);
        //表不存在创建表
        pec.setDatabaseSchemaUpdate ("true");
        //指定数据库
        pec.setDatabaseType ("mysql");
        pec.setTransactionManager (platformTransactionManager);
        //历史变量
        pec.setHistory ("full");
        pec.setActivityFontName ("宋体");
        pec.setAnnotationFontName ("宋体");
        pec.setLabelFontName ("宋体");
        pec.setProcessDiagramGenerator (new DefaultProcessDiagramGenerator ());
        return pec;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean (ProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean ();
        processEngineFactoryBean.setProcessEngineConfiguration ((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService repositoryService (ProcessEngine processEngine) {
        return processEngine.getRepositoryService ();
    }

    @Bean
    public RuntimeService runtimeService (ProcessEngine processEngine) {
        return processEngine.getRuntimeService ();
    }

    @Bean
    public TaskService taskService (ProcessEngine processEngine) {
        return processEngine.getTaskService ();
    }

    @Bean
    public HistoryService historyService (ProcessEngine processEngine) {
        return processEngine.getHistoryService ();
    }

    @Bean
    public FormService formService (ProcessEngine processEngine) {
        return processEngine.getFormService ();
    }

    @Bean
    public IdentityService identityService (ProcessEngine processEngine) {
        return processEngine.getIdentityService ();
    }

    @Bean
    public ManagementService managementService (ProcessEngine processEngine) {
        return processEngine.getManagementService ();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService (ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService ();
    }

    @Bean
    public ObjectMapper objectMapper () {
        return new ObjectMapper ();
    }

}
