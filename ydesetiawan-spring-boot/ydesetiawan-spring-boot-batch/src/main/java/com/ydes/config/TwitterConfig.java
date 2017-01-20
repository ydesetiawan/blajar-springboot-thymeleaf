package com.ydes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Configuration
@ConfigurationProperties(prefix = "spring.social.twitter")
public class TwitterConfig {

    private String appId;

    private String appSecret;

    @Bean
    public Twitter twitter() {
        return new TwitterTemplate(appId, appSecret);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

}
