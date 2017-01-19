/* Copyright (C) 2015 ASYX International B.V. All rights reserved. */
package com.ydes.config;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.ydes.common.util.AppsLifecycleShutdownEvent;

/**
 * @author frankr
 * @version 1.0, May 18, 2015
 * @since
 */
public class ShutdownHook extends Thread {

    private static Logger log = LoggerFactory.getLogger(ShutdownHook.class);

    private ApplicationContext context;

    public ShutdownHook(ApplicationContext context) {
        super();
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        log.info("Initiating shutdown...");
        context.publishEvent(new AppsLifecycleShutdownEvent(Application.class));

        try {
            log.info("Stopping embedded servlet container...");
            ((EmbeddedWebApplicationContext) context)
                    .getEmbeddedServletContainer().stop();
        } catch (Exception e) {
            log.warn("Error stopping embedded servlet container", e);
        }

        try {
            log.info("Stopping Quartz scheduler...");
            Scheduler scheduler = context.getBean(Scheduler.class);
            scheduler.standby();
        } catch (Exception e) {
            log.warn("Error stopping Quartz scheduler", e);
        }

        try {
            log.info("Stopping Spring Batch jobs...");
            JobOperator jobOperator = context.getBean(JobOperator.class);
            for (String jobName : jobOperator.getJobNames()) {
                for (Long executionId : jobOperator
                        .getRunningExecutions(jobName)) {
                    log.info("Stopping running job: " + jobName
                            + ", executionId: " + executionId);
                    jobOperator.stop(executionId.longValue());
                }
            }
        } catch (Exception e) {
            log.warn("Error stopping Spring Batch jobs", e);
        }

        log.info("Closing Spring application context...");
        ((AbstractApplicationContext) context).close();
    }

}
