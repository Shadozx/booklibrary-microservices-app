package com.shadoww.jwtsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUser extends UserDetails {
    Long getUserId();
    String getRole();
    String getUsername();
}