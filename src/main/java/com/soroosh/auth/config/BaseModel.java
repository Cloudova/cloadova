package com.soroosh.auth.config;

import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

public class BaseModel {
    @CreatedDate
    protected Timestamp createdAt;
}
