package com.soroosh.auth.user.exceptions;

import com.soroosh.auth.commons.exceptions.InvalidArgumentException;

public class InvalidOtpException extends InvalidArgumentException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
