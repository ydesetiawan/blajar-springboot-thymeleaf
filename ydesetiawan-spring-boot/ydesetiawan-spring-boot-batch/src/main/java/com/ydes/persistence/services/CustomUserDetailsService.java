package com.ydes.persistence.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydes.persistence.model.UserRole;
import com.ydes.persistence.repository.UserRepository;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        com.ydes.persistence.model.User user = userRepository
                .findByUsername(username);
        List<GrantedAuthority> authorities = buildUserAuthority(user
                .getUserRole());

        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(
            com.ydes.persistence.model.User user,
            List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority("ROLE_"
                    + userRole.getRole()));
        }

        return new ArrayList<>(setAuths);
    }

}
