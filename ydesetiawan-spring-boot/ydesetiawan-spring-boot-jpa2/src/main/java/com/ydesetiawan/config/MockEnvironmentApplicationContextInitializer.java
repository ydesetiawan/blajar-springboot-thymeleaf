package com.ydesetiawan.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockEnvironment;

public class MockEnvironmentApplicationContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.setProperty("hibernate.dialect",
                "org.hibernate.dialect.H2Dialect");
        mockEnvironment.setProperty("hibernate.show_sql", "false");
//        mockEnvironment.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        mockEnvironment.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        applicationContext.setEnvironment(mockEnvironment);
    }
}