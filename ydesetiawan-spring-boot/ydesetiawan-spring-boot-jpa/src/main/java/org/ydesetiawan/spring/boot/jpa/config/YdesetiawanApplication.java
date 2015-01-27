package org.ydesetiawan.spring.boot.jpa.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class YdesetiawanApplication {
	public static void main(String[] args) {
		SpringApplication.run(YdesetiawanApplication.class, args);
	}
}
