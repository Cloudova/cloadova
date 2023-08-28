package com.soroosh.auth.user.http.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public record SendOtpRequest(@NotBlank(message = "identifier is required") @Email String identifier) {
}
