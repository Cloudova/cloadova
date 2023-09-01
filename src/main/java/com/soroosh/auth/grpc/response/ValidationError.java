package com.soroosh.auth.grpc.response;

import lombok.Builder;

@Builder
public class ValidationError {
    private String property;
    private String message;
}
