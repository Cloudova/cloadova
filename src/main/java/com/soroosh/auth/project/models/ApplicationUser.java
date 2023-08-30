package com.soroosh.auth.project.models;

import com.soroosh.auth.commons.models.BaseUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser extends BaseUser {

    @Column
    protected String username;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Builder.Default
    private boolean isActive = true;

    @Column
    @Builder.Default
    private boolean banned = false;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonIgnore
    @ToString.Exclude
    private Application application;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<MetaData> metadata;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("APP_" + this.application.getName()));
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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
        ApplicationUser user = (ApplicationUser) o;
        return id != null && Objects.equals(id, user.id);
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.putIfAbsent("id", this.getId());
        map.putIfAbsent("name", this.getName());
        map.putIfAbsent("email", this.getEmail());
        return map;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
