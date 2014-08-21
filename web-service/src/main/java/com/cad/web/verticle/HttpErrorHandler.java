package com.cad.web.verticle;

import org.vertx.java.core.json.JsonObject;

import static com.cad.web.verticle.ResponseStatusCode.INTERNAL_SERVER_ERROR;

public class HttpErrorHandler {

    private ResponseSender responseSender;

    public HttpErrorHandler(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    public void errorResponse(String message) {
        errorResponse(INTERNAL_SERVER_ERROR, message);
    }

    public void errorResponse(ResponseStatusCode statusCode, String message) {
        JsonObject jo = new JsonObject();
        jo.putValue("error", message);
        String data = jo.toString();
        responseSender.sendErrorResponse(statusCode, data);
    }

    public void errorResponse(String message, Exception e) {
        JsonObject jo = new JsonObject();
        jo.putValue("error", message + e.getMessage());
        String data = jo.toString();
        responseSender.sendErrorResponse(INTERNAL_SERVER_ERROR, data);
    }

    public void errorResponse(String message, RuntimeException e) {
        JsonObject jo = new JsonObject();
        jo.putValue("error", message + e.getMessage());
        String data = jo.toString();
        responseSender.sendErrorResponse(INTERNAL_SERVER_ERROR, data);
    }
}
