package com.soroosh.auth.project.models;

import com.soroosh.auth.user.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "applications")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {

    @Id
    @Column(name = "id", nullable = false)
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(unique = true)
    private String subdomain;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @JsonIgnore
    private String secret;

    @JoinColumn(nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @JoinColumn(name = "application_id")
    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<ApplicationUser> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Application project = (Application) o;
        return id != null && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
