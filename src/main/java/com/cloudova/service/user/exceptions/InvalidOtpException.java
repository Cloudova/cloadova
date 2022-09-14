package com.cloudova.service.user.exceptions;

import com.cloudova.service.commons.exceptions.InvalidArgumentException;

public class InvalidOtpException extends InvalidArgumentException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
