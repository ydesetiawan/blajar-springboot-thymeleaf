package com.ydesetiawan.config;

import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.ldap.ppolicy.PasswordPolicyAwareContextSource;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(LdapSettings.class)
@Profile("ldap")
public class LdapSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger log = Logger.getLogger(LdapSecurityConfig.class);

    @Autowired
    private LdapSettings ldapSettings;

    @Autowired
    private UserDetailsContextMapper customUserDetailsContextMapper;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private ObjectPostProcessor<Object> objectPostProcessor;

    @Autowired
    @Qualifier("authenticationProvider")
    private AuthenticationProvider authenticationProvider;
    @Autowired(required = false)
    @Qualifier("secondaryAuthenticationProvider")
    private AuthenticationProvider secondaryAuthenticationProvider;
    @Autowired(required = false)
    @Qualifier("activeDirectoryAuthenticationProvider")
    private AuthenticationProvider activeDirectoryAuthenticationProvider;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Value("${login.maximum_sessions}")
    private int maximumSessions;

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManagerBuilder auth = new AuthenticationManagerBuilder(
                objectPostProcessor);
        Assert.notNull(auth);
        Assert.notNull(authenticationProvider());
        if (secondaryAuthenticationProvider != null) {
            auth = auth.authenticationProvider(authenticationProvider)
                    .authenticationProvider(secondaryAuthenticationProvider);
            log.info("Using authentication providers: [primary: "
                    + authenticationProvider + ", secondary: "
                    + secondaryAuthenticationProvider + "]");
        } else if (activeDirectoryAuthenticationProvider != null) {
            auth = auth.authenticationProvider(authenticationProvider)
                    .authenticationProvider(
                            activeDirectoryAuthenticationProvider);
            log.info("Using authentication providers: [primary: "
                    + authenticationProvider + ", secondary: "
                    + activeDirectoryAuthenticationProvider + "]");
        } else {
            auth = auth.authenticationProvider(authenticationProvider);
            log.info("Using authentication providers: [primary: "
                    + authenticationProvider + "]");
        }
        return auth.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() throws Exception {
        BindAuthenticator authenticator = new CustomBindAuthenticator(
                contextSource());
        LdapAuthoritiesPopulator populator = new NullLdapAuthoritiesPopulator();
        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(
                authenticator, populator);
        provider.setUserDetailsContextMapper(customUserDetailsContextMapper);
        authenticator
                .setUserDnPatterns(ldapSettings.getUserDnPatternsAsArray());
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/images/**", "/img/**",
                        "/js/**", "/public/**", "/signup", "/activation/**",
                        "/activation-success", "/resetpassword").permitAll();

        http.formLogin().loginPage("/login").defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/login?error=failure").permitAll().and()
                .authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/home/**").access("hasRole('ADMIN')").and()
                .exceptionHandling().accessDeniedPage("/403");
        ;
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
        http.authorizeRequests().antMatchers("/login/changepass")
                .hasRole("CHANGEPASS");
        http.authorizeRequests()
                .antMatchers("/login/secondfactor", "/login/secondfactor/**")
                .hasRole("SECONDFACTOR");
        http.authorizeRequests()
                .antMatchers("/login/termsaccept", "/login/termsaccept/**")
                .hasRole("TERMSACCEPT");
        http.authorizeRequests()
                .antMatchers("/support/switchUser", "/support/switchUserExit")
                .hasRole("SUPPORT");
        http.authorizeRequests().antMatchers("/", "/**").hasRole("USER");

        http.addFilter(customSwitchUserFilter());

        http.sessionManagement().maximumSessions(maximumSessions)
                .expiredUrl("/login?error=expired")
                .sessionRegistry(sessionRegistry).and()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Bean
    public BaseLdapPathContextSource contextSource() throws Exception {
        PasswordPolicyAwareContextSource contextSource = new PasswordPolicyAwareContextSource(
                ldapSettings.getProviderUrl());
        contextSource.setUserDn(ldapSettings.getUserDn());
        contextSource.setPassword(ldapSettings.getPassword());
        return contextSource;
    }

    // @Bean
    // public AuthenticationFailureHandler customAuthenticationFailureHandler()
    // {
    // CustomAuthenticationFailureHandler handler = new
    // CustomAuthenticationFailureHandler();
    // handler.setExceptionMappings(ldapSettings.getExceptionMappings());
    // return handler;
    // }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler();
        return customAuthenticationSuccessHandler;
    }

    @Bean
    public SwitchUserFilter customSwitchUserFilter() {
        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
        switchUserFilter.setUserDetailsService(customUserDetailsService);
        switchUserFilter.setSwitchUserUrl("/support/switchUser");
        switchUserFilter.setExitUserUrl("/support/switchUserExit");
        switchUserFilter.setTargetUrl("/");
        switchUserFilter.setUsernameParameter("username");
        return switchUserFilter;
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                log.debug("Session created: " + event);
                super.sessionCreated(event);
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                log.debug("Session destroyed: " + event);
                super.sessionDestroyed(event);
            }
        };
    }

    @Bean
    public LdapTemplate ldapTemplate() throws Exception {
        return new LdapTemplate(contextSource());
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();

    }
}
