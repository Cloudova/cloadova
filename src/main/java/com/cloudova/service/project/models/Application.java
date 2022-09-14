package com.cloudova.service.project.models;

import com.cloudova.service.user.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="applications")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String subdomain;

    @Column
    private String name;

    @Column
    private String description;

    @JoinColumn
    @OneToOne
    @JsonIgnore
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
