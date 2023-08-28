package com.soroosh.auth.config.identifierMatcher;

public class InvalidIdentifier extends RuntimeException{

    public InvalidIdentifier(String message) {
        super(message);
    }
}
