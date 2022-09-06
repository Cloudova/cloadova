package com.cloudova.service.user.http.resources;

public record OtpSent(
        boolean status,
        String verificationID
) {
}
