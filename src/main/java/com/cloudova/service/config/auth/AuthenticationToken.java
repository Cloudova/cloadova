package com.cloudova.service.config.auth;

import com.cloudova.service.user.models.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private final User principal;
    private final String credentials;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, User user, String jwtToken) {
        super(authorities);
        this.principal = user;
        this.credentials = jwtToken;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
