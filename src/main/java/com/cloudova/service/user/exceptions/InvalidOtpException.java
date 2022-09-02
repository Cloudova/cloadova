package com.cloudova.service.user.exceptions;

public class InvalidOtpException  extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
