package com.soroosh.auth.user.http.requests;

import javax.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String email, @NotEmpty String password) {
}
