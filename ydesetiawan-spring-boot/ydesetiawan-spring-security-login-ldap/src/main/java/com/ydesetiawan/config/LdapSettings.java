package com.ydesetiawan.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@Configuration
@ConfigurationProperties(prefix = "ldap", ignoreUnknownFields = true)
@Profile("ldap")
public class LdapSettings {

    private Map<String, String> exceptionMappings;

    private String password;

    private String passwordPolicySubentry;

    private String providerUrl;

    private String secondaryPassword;

    private String secondaryProviderUrl;

    private String secondaryUserDn;

    private Map<String, String> secondaryUserDnPatterns;

    private String userDn;

    private Map<String, String> userDnPatterns;

    public Map<String, String> getExceptionMappings() {
        return exceptionMappings;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordPolicySubentry() {
        return passwordPolicySubentry;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public String getSecondaryPassword() {
        return secondaryPassword;
    }

    public String getSecondaryProviderUrl() {
        return secondaryProviderUrl;
    }

    public String getSecondaryUserDn() {
        return secondaryUserDn;
    }

    public Map<String, String> getSecondaryUserDnPatterns() {
        return secondaryUserDnPatterns;
    }

    public String[] getSecondaryUserDnPatternsAsArray() {
        return secondaryUserDnPatterns.values().toArray(
                new String[secondaryUserDnPatterns.size()]);
    }

    public String getUserDn() {
        return userDn;
    }

    public Map<String, String> getUserDnPatterns() {
        return userDnPatterns;
    }

    public String[] getUserDnPatternsAsArray() {
        return userDnPatterns.values().toArray(
                new String[userDnPatterns.size()]);
    }

    public void setExceptionMappings(Map<String, String> exceptionMappings) {
        this.exceptionMappings = exceptionMappings;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordPolicySubentry(String passwordPolicySubentry) {
        this.passwordPolicySubentry = passwordPolicySubentry;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public void setSecondaryPassword(String secondaryPassword) {
        this.secondaryPassword = secondaryPassword;
    }

    public void setSecondaryProviderUrl(String secondaryProviderUrl) {
        this.secondaryProviderUrl = secondaryProviderUrl;
    }

    public void setSecondaryUserDn(String secondaryUserDn) {
        this.secondaryUserDn = secondaryUserDn;
    }

    public void setSecondaryUserDnPatterns(
            Map<String, String> secondaryUserDnPatterns) {
        this.secondaryUserDnPatterns = secondaryUserDnPatterns;
    }

    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public void setUserDnPatterns(Map<String, String> userDnPatterns) {
        this.userDnPatterns = userDnPatterns;
    }

    @Override
    public String toString() {
        return "LdapSettings [exceptionMappings=" + exceptionMappings
                + ", password=" + "***" + ", passwordPolicySubentry="
                + passwordPolicySubentry + ", providerUrl=" + providerUrl
                + ", secondaryPassword=" + "***" + ", secondaryProviderUrl="
                + secondaryProviderUrl + ", secondaryUserDn=" + secondaryUserDn
                + ", secondaryUserDnPatterns=" + secondaryUserDnPatterns
                + ", userDn=" + userDn + ", userDnPatterns=" + userDnPatterns
                + "]";
    }
}
