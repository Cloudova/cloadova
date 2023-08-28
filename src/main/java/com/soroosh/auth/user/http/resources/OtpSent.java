package com.soroosh.auth.user.http.resources;

public record OtpSent(
        boolean status,
        String verificationID
) {
}
