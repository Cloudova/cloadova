package com.soroosh.auth.user.models;

import com.soroosh.auth.commons.models.BaseUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column(unique = true)
    private String mobile;

    @Column
    private Role role;

    @Column
    @Builder.Default
    private boolean isActive = true;

    @Column
    @Builder.Default
    private boolean banned = false;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.role.getAuthority();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email != null ? this.email : this.mobile;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.putIfAbsent("id", this.getId());
        map.putIfAbsent("name", this.getName());
        map.putIfAbsent("username", this.getUsername());
        map.putIfAbsent("authorities", this.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        map.putIfAbsent("role", this.getRole().name());
        return map;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.banned;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
