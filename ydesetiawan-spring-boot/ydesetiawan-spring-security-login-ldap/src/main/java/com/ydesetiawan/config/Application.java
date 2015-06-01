package com.ydesetiawan.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = { "com.ydesetiawan" })
public class Application {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }

}