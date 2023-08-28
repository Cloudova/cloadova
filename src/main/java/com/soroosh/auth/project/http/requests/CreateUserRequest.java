package com.soroosh.auth.project.http.requests;

import com.soroosh.auth.project.dto.ApplicationUserDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateUserRequest(@NotEmpty String code, @NotEmpty String identifier, @NotNull @Validated ApplicationUserDto user) {
}
