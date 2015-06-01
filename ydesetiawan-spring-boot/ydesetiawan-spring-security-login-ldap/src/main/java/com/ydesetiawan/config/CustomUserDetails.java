package com.ydesetiawan.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.Name;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.ppolicy.PasswordPolicyErrorStatus;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;

import com.ydesetiawan.persistence.model.User;

/**
 * @author edys
 * @version 1.0, Jan 6, 2015
 * @since
 */
public class CustomUserDetails implements UserDetails {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(CustomUserDetails.class);

    private static final long serialVersionUID = -1338282990755794283L;

    private Map<String, String> announcements;

    private Set<GrantedAuthority> authorities;
    private Set<GrantedAuthority> secondFactorAuthorities;
    private Set<GrantedAuthority> termsAcceptAuthorities;

    private Name dn;

    private String password;

    private PasswordPolicyResponseControl passwordPolicyResponseControl;

    private String perspective;

    private User user;

    private String username;

    private boolean requireSecondFactor;
    private boolean requireTermsAccept;

    public CustomUserDetails(String username, Set<GrantedAuthority> authorities) {
        super();
        this.username = username;
        this.authorities = authorities;
    }

    public CustomUserDetails(String username, String password,
            Set<GrantedAuthority> authorities) {
        super();
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public void acceptTerms() {
        this.requireTermsAccept = false;
        enableAuthoritiesForTermsAccept();
    }

    public void addAnnouncement(String key, String message) {
        if (announcements == null) {
            announcements = new HashMap<String, String>();
        }
        announcements.put(key, message);
    }

    private void disableAuthoritiesForSecondFactor() {
        secondFactorAuthorities = authorities;
        authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SECONDFACTOR"));
    }

    private void disableAuthoritiesForTermsAccept() {
        if (requireSecondFactor) {
            termsAcceptAuthorities = secondFactorAuthorities;
            secondFactorAuthorities = new HashSet<GrantedAuthority>();
            secondFactorAuthorities.add(new SimpleGrantedAuthority(
                    "ROLE_TERMSACCEPT"));
        } else {
            termsAcceptAuthorities = authorities;
            authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_TERMSACCEPT"));
        }
    }

    private void enableAuthoritiesForSecondFactor() {
        authorities = secondFactorAuthorities;
    }

    private void enableAuthoritiesForTermsAccept() {
        authorities = termsAcceptAuthorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomUserDetails other = (CustomUserDetails) obj;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    public Map<String, String> getAnnouncements() {
        return announcements;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getAuthorities
     * ()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Name getDn() {
        return dn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    public PasswordPolicyResponseControl getPasswordPolicyResponseControl() {
        return passwordPolicyResponseControl;
    }

    public String getPerspective() {
        return perspective;
    }

    public User getUser() {
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired
     * ()
     */
    @Override
    public boolean isAccountNonExpired() {
        if (passwordPolicyResponseControl != null) {
            return !passwordPolicyResponseControl.isExpired();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked
     * ()
     */
    @Override
    public boolean isAccountNonLocked() {
        if (passwordPolicyResponseControl != null) {
            return !passwordPolicyResponseControl.isLocked();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.UserDetails#
     * isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        if (passwordPolicyResponseControl != null) {
            return !passwordPolicyResponseControl.isExpired();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if (passwordPolicyResponseControl != null) {
            return !passwordPolicyResponseControl.isLocked();
        }
        return true;
    }

    public boolean isRequireSecondFactor() {
        return requireSecondFactor;
    }

    public boolean isRequireTermsAccept() {
        return requireTermsAccept;
    }

    public void removeAnnouncement(String key) {
        if (announcements == null) {
            return;
        }
        announcements.remove(key);
    }

    public void requireSecondFactor() {
        this.requireSecondFactor = true;
        disableAuthoritiesForSecondFactor();
    }

    public void requireTermsAccept() {
        this.requireTermsAccept = true;
        disableAuthoritiesForTermsAccept();
    }

    public void setAnnouncements(Map<String, String> announcements) {
        this.announcements = announcements;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Xchange3UserDetails [authorities=" + authorities + ", dn=" + dn
                + ", passwordPolicyResponseControl="
                + passwordPolicyResponseControl + ", perspective="
                + perspective + ", user=" + user + ", username=" + username
                + "]";
    }

    public boolean validateSecondFactor(int code) {
        try {
            if (TotpAuthenticatorUtil.verifyCode(user.getTotpSecret(), code, 2)) {
                this.requireSecondFactor = false;
                enableAuthoritiesForSecondFactor();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public CustomUserDetails withDn(Name dn) {
        this.dn = dn;
        return this;
    }

    public CustomUserDetails withPasswordPolicyResponseControl(
            PasswordPolicyResponseControl passwordPolicyResponseControl) {
        this.passwordPolicyResponseControl = passwordPolicyResponseControl;
        if (PasswordPolicyErrorStatus.CHANGE_AFTER_RESET
                .equals(passwordPolicyResponseControl.getErrorStatus())) {
            authorities.clear();
            authorities.add(new SimpleGrantedAuthority("ROLE_CHANGEPASS"));
        }
        return this;
    }
}
