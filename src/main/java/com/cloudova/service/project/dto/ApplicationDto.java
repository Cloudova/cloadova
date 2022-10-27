package com.cloudova.service.project.dto;

import javax.validation.constraints.NotEmpty;

public record ApplicationDto(
        @NotEmpty
        String name,
        @NotEmpty
        String subdomain,
        String description
){}