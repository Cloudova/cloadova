package com.cloudova.service.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LimitExceedException extends ResponseStatusException {
    /**
     * Constructor with a response status.
     *
     */
    public LimitExceedException(String reason) {
        super(HttpStatus.FORBIDDEN,reason);
    }
}
