package com.soroosh.auth.config;

import com.soroosh.auth.commons.http.HttpStatusResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new HttpStatusResponse<>(false, ex.getMessage()),
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(value
            = {AuthenticationException.class})
    protected ResponseEntity<Object> handleUnauthorized(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new HttpStatusResponse<>(false, ex.getMessage()),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value
            = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleForbiddenAccess(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new HttpStatusResponse<>(false, ex.getMessage()),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }


    @ExceptionHandler(value
            = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleNotFound(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new HttpStatusResponse<>(false, "Requested Resource Not Found"),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


}
