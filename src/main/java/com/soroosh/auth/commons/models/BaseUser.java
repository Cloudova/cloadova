package com.soroosh.auth.commons.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public abstract class BaseUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    protected String id;

    @Column
    protected String username;

    @LastModifiedDate
    protected Timestamp updatedAt;

    @CreationTimestamp
    protected Timestamp createdAt;

    @Version
    @Column(name = "version")
    private Integer version;

    @Override
    public String getUsername(){
        return this.username;
    }

}
