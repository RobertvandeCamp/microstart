package com.cad.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

public class FourOFourHandler implements Handler<HttpServerRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(FourOFourHandler.class);
    public static final int STATUS_CODE_NOT_FOUND = 404;

    @Override
    public void handle(HttpServerRequest request) {
        HttpServerResponse response = request.response();
        LOG.debug("No handler for this request [" + request.path() + "], returning 404");
        response.putHeader("content-type", "text/plain");
        response.setStatusCode(STATUS_CODE_NOT_FOUND);
        response.end();
    }

}