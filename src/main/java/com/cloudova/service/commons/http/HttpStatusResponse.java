package com.cloudova.service.commons.http;

public class HttpStatusResponse<T> {
    private boolean status;
    private String message;
    private T data;

    public HttpStatusResponse(boolean status) {
        this.status = status;
    }

    public HttpStatusResponse(boolean status, T data) {
        this.status = status;
        this.data = data;
    }

    public HttpStatusResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatusResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
