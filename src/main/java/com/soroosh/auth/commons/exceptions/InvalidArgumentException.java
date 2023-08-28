package com.soroosh.auth.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidArgumentException extends ResponseStatusException {
    public InvalidArgumentException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
