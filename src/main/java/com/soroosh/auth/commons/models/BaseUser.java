package com.soroosh.auth.commons.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
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

    @Override
    public String getUsername(){
        return this.username;
    }

}
