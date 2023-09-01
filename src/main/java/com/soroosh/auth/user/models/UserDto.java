package com.soroosh.auth.user.models;

import com.soroosh.auth.user.validators.unique.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.checkerframework.common.value.qual.MinLen;

import java.io.Serializable;

@Builder
public record UserDto(@NotEmpty String firstName,
                      @NotEmpty String lastName,
                      @NotEmpty @Email @Unique(field = "email", object = "com.soroosh.auth.user.models.User") String email,
                      @NotEmpty @Unique(field = "mobile", object = "com.soroosh.auth.user.models.User") String mobile,
                      @NotEmpty @MinLen(8) String password) implements Serializable {
}
