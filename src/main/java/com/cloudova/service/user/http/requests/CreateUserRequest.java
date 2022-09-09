package com.cloudova.service.user.http.requests;

import com.cloudova.service.user.models.UserDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateUserRequest(@NotEmpty String code, @NotEmpty String identifier, @NotNull @Validated UserDto user) {
}
