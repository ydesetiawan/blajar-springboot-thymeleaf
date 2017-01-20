package com.ydes.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ydes.common.util.AppsLifecycleStartupEvent;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */

@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class,TwitterAutoConfiguration.class })
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = { "com.ydes" })
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Application.class);
        ApplicationContext context = app.run(args);
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(context));
        context.publishEvent(new AppsLifecycleStartupEvent(Application.class));
    }

}