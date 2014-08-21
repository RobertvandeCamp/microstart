package com.cad.web.verticle;

public enum ResponseStatusCode {

    OK(200), BAD_REQUEST(400), FORBIDDEN(403), NOT_FOUND(404), CONFLICT(409), INTERNAL_SERVER_ERROR(500);

    private int code;

    private ResponseStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}