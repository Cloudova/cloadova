package com.cloudova.service.config.identifierMatcher;

import java.util.function.Supplier;

public class InvalidIdentifier extends RuntimeException{

    public InvalidIdentifier(String message) {
        super(message);
    }
}
