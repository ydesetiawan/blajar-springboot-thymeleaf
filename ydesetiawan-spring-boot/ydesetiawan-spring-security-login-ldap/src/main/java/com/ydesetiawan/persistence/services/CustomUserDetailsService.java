package com.ydesetiawan.persistence.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydesetiawan.config.CustomUserDetails;
import com.ydesetiawan.persistence.model.UserRole;
import com.ydesetiawan.persistence.repository.UserRepository;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private static Logger log = Logger
            .getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.debug("Getting user details for: " + username);
        com.ydesetiawan.persistence.model.User user = null;
        try {
            user = userRepository.findByUsername(username);
            if (user == null) {
                log.error("Failed for user: " + username);
                throw new UsernameNotFoundException("User '" + username
                        + "' not found.");
            }
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            for (UserRole userRole : user.getUserRole()) {
                authorities.add(new SimpleGrantedAuthority(userRole.getRole()));
            }

            CustomUserDetails userDetails;
            userDetails = new CustomUserDetails(username,user.getPassword(), authorities);
            userDetails.setUser(user);

            if (user.getTotpSecret() != null) {
                userDetails.requireSecondFactor();
            }

            log.debug("Returning user with username: " + username
                    + ", authorities: " + authorities);

            return userDetails;
        } catch (Exception e) {
            log.debug("Error in retrieving user details", e);
        }
        return null;
    }

}
