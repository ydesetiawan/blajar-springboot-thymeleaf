package com.ydes.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */

@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class })
@Configuration
@ComponentScan(basePackages = { "com.ydes" })
public class Application {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }

}