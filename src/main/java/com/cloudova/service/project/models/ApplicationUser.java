package com.cloudova.service.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "application_users")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    @Builder.Default
    private boolean isActive = true;

    @Column
    @Builder.Default
    private boolean banned = false;

    @Column
    private String password;


    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<MetaData> metadata;

    @LastModifiedDate
    private Timestamp updatedAt;

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationUser user = (ApplicationUser) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
