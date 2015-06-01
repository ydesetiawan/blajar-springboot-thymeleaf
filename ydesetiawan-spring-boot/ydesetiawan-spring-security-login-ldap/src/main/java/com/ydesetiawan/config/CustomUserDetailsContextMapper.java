package com.ydesetiawan.config;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@Component
@Profile("ldap")
public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

    @SuppressWarnings("unused")
    private static Logger log = Logger
            .getLogger(CustomUserDetailsContextMapper.class);

    @Autowired
    UserDetailsService customUserDetailsService;

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx,
            String username, Collection<? extends GrantedAuthority> authorities) {

        PasswordPolicyResponseControl ppolicy = (PasswordPolicyResponseControl) ctx
                .getObjectAttribute(PasswordPolicyControl.OID);
        
        
        
        CustomUserDetails userDetails;
        if (ppolicy != null) {
            userDetails = ((CustomUserDetails) customUserDetailsService
                    .loadUserByUsername(username)).withDn(ctx.getDn())
                    .withPasswordPolicyResponseControl(ppolicy);
        } else {
            userDetails = ((CustomUserDetails) customUserDetailsService
                    .loadUserByUsername(username)).withDn(ctx.getDn());
            
        }
        return userDetails;
    }

    @Override
    public void mapUserToContext(UserDetails userDetails,
            DirContextAdapter dirContextAdapter) {
        throw new IllegalStateException("Not implemented");
    }
}
