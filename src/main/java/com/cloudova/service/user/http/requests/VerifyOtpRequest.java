package com.cloudova.service.user.http.requests;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @Email @NotBlank(message = "identifier is required")
        String identifier,
        @NotBlank
        @Length(min = 8, max = 8)
        String code
) {
}
