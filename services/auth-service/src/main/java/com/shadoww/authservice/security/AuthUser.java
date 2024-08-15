package com.shadoww.authservice.security;

import com.shadoww.authservice.model.Person;
import com.shadoww.jwtsecurity.security.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


public class AuthUser implements JwtUser {

    private final Person person;

    public AuthUser(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(person.getRole().getRoleName()));
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public Long getUserId() {
        return person.getId();
    }

    @Override
    public String getRole() {
        return person.getRole().name();
    }

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}