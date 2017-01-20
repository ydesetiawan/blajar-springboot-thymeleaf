package com.ydes.controller;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import com.ydes.common.util.AppsLifecycleEvent;
import com.ydes.common.util.AppsLifecycleStartupEvent;

/**
 * @author edys
 * @version 1.0, Jun 25, 2014
 * @since 3.1.0
 */
@Controller
public class AppsLifecycleController implements
        ApplicationListener<AppsLifecycleEvent> {

    private static Logger log = Logger.getLogger(AppsLifecycleController.class);

    @Autowired
    Scheduler scheduler;

    @Override
    public void onApplicationEvent(AppsLifecycleEvent event) {
        if (event instanceof AppsLifecycleStartupEvent) {

            log.info("Starting Quartz Scheduler...");
            try {
                scheduler.start();
            } catch (SchedulerException e) {
                log.warn("Failed to start scheduler", e);
            }
        }
    }
}
