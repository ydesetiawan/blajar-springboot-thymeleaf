package com.ydesetiawan.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
public class CustomAuthenticationFailureHandler extends
        ExceptionMappingAuthenticationFailureHandler implements
        AuthenticationFailureHandler {

    private static Logger log = Logger
            .getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.debug("Exception caught during authentication", exception);
        super.onAuthenticationFailure(request, response, exception);
    }
}
