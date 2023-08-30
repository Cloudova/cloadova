package com.soroosh.auth.user.http.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record SendOtpRequest(@NotBlank(message = "identifier is required") @Email String identifier) {
}
