package com.krab51.webapp.services.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomUserDetailsBuilder {
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private Integer id;
    private Boolean enabled;
    private Boolean accountNotExpired;
    private Boolean accountNotLocked;
    private Boolean credentialsNonExpired;

    public CustomUserDetailsBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public CustomUserDetailsBuilder setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public CustomUserDetailsBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public CustomUserDetailsBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public CustomUserDetailsBuilder setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public CustomUserDetailsBuilder setAccountNotExpired(Boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
        return this;
    }

    public CustomUserDetailsBuilder setAccountNotLocked(Boolean accountNotLocked) {
        this.accountNotLocked = accountNotLocked;
        return this;
    }

    public CustomUserDetailsBuilder setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public CustomUserDetails createCustomUserDetails() {
        return new CustomUserDetails(password, authorities, username, id, enabled, accountNotExpired, accountNotLocked, credentialsNonExpired);
    }
}