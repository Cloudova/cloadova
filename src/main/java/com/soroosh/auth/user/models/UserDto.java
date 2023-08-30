package com.soroosh.auth.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserDto(@NotEmpty String firstName,
                      @NotEmpty String lastName,
                      @NotEmpty @Email String email,
                      @NotEmpty String mobile,
                      @NotEmpty @Min(8) String password) implements Serializable {
}
