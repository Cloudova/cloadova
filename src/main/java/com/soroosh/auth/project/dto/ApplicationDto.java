package com.soroosh.auth.project.dto;

import jakarta.validation.constraints.NotEmpty;

public record ApplicationDto(
        @NotEmpty
        String name,
        @NotEmpty
        String subdomain,
        String description
){}