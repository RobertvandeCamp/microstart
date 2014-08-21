package com.cad.web.verticle;

import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServerResponse;

public class ResponseSender {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseSender.class);
    public static final String UTF_8 = "UTF-8";
    private static final String COOKIE_EXPIRATION = "-1";
    private static final String CONTENT_TYPE = "content-type";
    private static final String CONTENT_TYPE_APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
    private static final String NO_CACHE_NO_STORE = "no-cache, no-store";
    private HttpServerResponse response;
    private VertxWrapper vertx;
    private Long requestTimeoutTimerId;

    public ResponseSender(HttpServerResponse response) {
        this.response = response;
    }

    public void sendResponse(ResponseStatusCode statusCode, String message) {
        cancelRequestTimeoutTimer();
        if (response == null) {
            logResponseAlreadyCommitted(String.valueOf(statusCode.getCode()), message);
            return;
        }
        response.putHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON_CHARSET_UTF_8);
        response.putHeader(HttpHeaders.Names.CACHE_CONTROL, NO_CACHE_NO_STORE);
        response.putHeader(HttpHeaders.Names.EXPIRES, COOKIE_EXPIRATION);
        response.setStatusCode(statusCode.getCode());
        response.end(message, UTF_8);
        response = null;
    }

    public void sendErrorResponse(ResponseStatusCode statusCode, String message) {
        cancelRequestTimeoutTimer();
        if (response == null) {
            logResponseAlreadyCommitted(String.valueOf(statusCode.getCode()), message);
            return;
        }
        response.putHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatusCode(statusCode.getCode());
        response.end(message, UTF_8);
        response = null;

    }

    private void cancelRequestTimeoutTimer() {
        if (requestTimeoutTimerId != null) {
            vertx.getWrappedVertx().cancelTimer(requestTimeoutTimerId);
        }
    }

    private void logResponseAlreadyCommitted(String statusCode, String message) {
        LOG.error("ResponseSender called while response already committed. Statuscode: " + statusCode + ", message: " + message);
    }

    public void setVertx(VertxWrapper vertx) {
        this.vertx = vertx;
    }

    public void setRequestTimeoutTimerId(Long requestTimeoutTimerId) {
        this.requestTimeoutTimerId = requestTimeoutTimerId;
    }

}
