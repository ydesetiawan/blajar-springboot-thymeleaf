package com.ydesetiawan.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.ppolicy.PasswordPolicyErrorStatus;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
public class CustomAuthenticationSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private static final String PASSWORD_WARNING_KEY = "password_warning";
    private static final String PASSWORD_WARNING_MSG = "Your password will expire %s, click <a href=\"/changepass\">here</a> to change.";

    @SuppressWarnings("unused")
    private static Logger log = Logger
            .getLogger(CustomAuthenticationSuccessHandler.class);

    private void addPasswordExpiryWarning(CustomUserDetails userDetails,
            PasswordPolicyResponseControl ppolicy) {
        int seconds = ppolicy.getTimeBeforeExpiration();
        int days = seconds / (60 * 60 * 24);
        seconds = seconds % (60 * 60 * 24);
        int hours = seconds / (60 * 60);
        String feedback;
        if (days > 0) {
            feedback = "in " + days + " days";
        } else if (hours > 0) {
            feedback = "in " + hours + " hours";
        } else {
            feedback = "very soon";
        }
        userDetails.addAnnouncement(PASSWORD_WARNING_KEY,
                String.format(PASSWORD_WARNING_MSG, feedback));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication
                .getPrincipal();
        PasswordPolicyResponseControl ppolicy = userDetails
                .getPasswordPolicyResponseControl();

        if (ppolicy != null
                && PasswordPolicyErrorStatus.CHANGE_AFTER_RESET.equals(ppolicy
                        .getErrorStatus())) {
            response.sendRedirect("/login/changepass");
            return;
        }

        if (userDetails.isRequireSecondFactor()) {
            response.sendRedirect("/login/secondfactor");
            return;
        }

        if (userDetails.isRequireTermsAccept()) {
            response.sendRedirect("/login/termsaccept");
            return;
        }

        if (ppolicy != null) {
            addPasswordExpiryWarning(userDetails, ppolicy);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
