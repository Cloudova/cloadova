package com.cloudova.service.user.http.requests;

import javax.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String email, @NotEmpty String password) {
}
