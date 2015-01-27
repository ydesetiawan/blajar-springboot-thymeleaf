package org.ydesetiawan.spring.boot.jpa.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since 
 */
@Configuration
@Profile("mysql")
public class MysqlDataSourceConfig {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setInitialSize(Integer.parseInt(env
                .getProperty("jdbc.pool.initialsize")));
        dataSource.setMaxActive(Integer.parseInt(env
                .getProperty("jdbc.pool.maxactive")));
        dataSource.setMaxIdle(Integer.parseInt(env
                .getProperty("jdbc.pool.maxidle")));
        dataSource.setMinIdle(Integer.parseInt(env
                .getProperty("jdbc.pool.minidle")));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(env
                .getProperty("jdbc.pool.testonborrow")));
        dataSource.setTestOnReturn(Boolean.parseBoolean(env
                .getProperty("jdbc.pool.testonreturn")));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(env
                .getProperty("jdbc.pool.testwhileidle")));
        return dataSource;
    }

}
