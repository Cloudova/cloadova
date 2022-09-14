package com.cloudova.service.project.http.requests;

import javax.validation.constraints.NotEmpty;

public record CreateApplicationRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String subdomain,
        String description
){}