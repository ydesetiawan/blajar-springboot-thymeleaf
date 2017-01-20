package com.ydes.batch;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author edys
 * @version 1.0, May 7, 2014
 * @since 3.0.0
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BatchConfig.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        Map<String, Object> schedulerContextMap = new HashMap<>();
        schedulerContextMap.put("jobLocator", jobLocator);
        schedulerContextMap.put("jobLauncher", jobLauncher);
        schedulerContextMap.put("jobExplorer", jobExplorer);
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setAutoStartup(false);
        schedulerFactory.setSchedulerContextAsMap(schedulerContextMap);
        return schedulerFactory;
    }
}
