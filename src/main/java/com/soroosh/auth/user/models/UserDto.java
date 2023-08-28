package com.soroosh.auth.user.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public record UserDto(@NotEmpty String firstName,
                      @NotEmpty String lastName,
                      @NotEmpty @Email String email,
                      @NotEmpty String mobile,
                      @NotEmpty @Min(8) String password) implements Serializable {
}
