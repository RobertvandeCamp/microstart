package com.cad.web.verticle;

public interface ErrorCallback {

    static enum ErrorResponse {
        NOT_FOUND, INTERNAL_ERROR, ILLEGAL, NOT_ALLOWED, CONFLICT
    }

    void error(ErrorResponse errorResponse, String message);
}
