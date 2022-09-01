package com.cloudova.service.user.models;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    ADMIN(
            "ROLE_ADMIN",
            "create:user",
            "delete:user",
            "ban:user",
            "activate:user",
            "create:project",
            "disable:project",
            "delete_project",
            "*"
    ), USER(
            "ROLE_USER",
            "create:project",
            "delete:project",
            "update_project"
    );

    private final String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }


    public Collection<GrantedAuthority> getAuthority() {
        return Stream.of(this.authorities).map((item) -> (GrantedAuthority) () -> item).collect(Collectors.toList());
    }
}
