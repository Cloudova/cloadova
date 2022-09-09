package com.cloudova.service.user.http.requests;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @Email
        @NotBlank(message = "identifier is required")
        String identifier,
        @NotBlank
        String code
) {
}
