package com.soroosh.auth.user.http.requests;

import com.soroosh.auth.user.models.UserDto;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotEmpty String code, @NotEmpty String identifier, @NotNull @Validated UserDto user) {
}
