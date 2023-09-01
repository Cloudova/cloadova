package com.soroosh.auth.commons.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

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
