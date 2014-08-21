package com.cad.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

public class WebRequestHandlerFactory implements Handler<HttpServerRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(WebRequestHandlerFactory.class);
    public static final int DEFAULT_REQUEST_TIMEOUT_IN_MS = 20000;
    private VertxWrapper vertx;
    private Class<? extends RequestHandler> clazz;
    private long requestTimeoutInMS = DEFAULT_REQUEST_TIMEOUT_IN_MS;

    public WebRequestHandlerFactory(VertxWrapper vertx, JsonObject containerConfig,
                                    Class<? extends RequestHandler> clazz) {
        this.vertx = vertx;
        this.clazz = clazz;
    }

    @Override
    public void handle(HttpServerRequest request) {
        final ResponseSender responseSender = new ResponseSender(request.response());
        responseSender.setVertx(vertx);

        // Initialize request timeout mechanism.
        Long timerId = vertx.getWrappedVertx().setTimer(requestTimeoutInMS, new Handler<Long>() {
            @Override
            public void handle(Long event) {
                // Handler request timed out. Log and send error response.
                LOG.error("Request timed out for " + clazz.getName());
                new HttpErrorHandler(responseSender).errorResponse("Unexpected error - request timed out");
            }
        });
        responseSender.setRequestTimeoutTimerId(timerId);

        handleRequest(request, responseSender);
    }

    private void handleRequest(HttpServerRequest request, ResponseSender responseSender) {
        try {
            RequestHandler handler = clazz.newInstance();
            handler.setVertx(vertx);
            handler.setResponseSender(responseSender);
            handler.handle(request);
        } catch (InstantiationException e) {
            throw new RequestHandlerInstantationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RequestHandlerAccessException(e.getMessage(), e);
        } catch (RequestHandler.RequestParameterMissing | RequestHandler.HeaderParameterMissing e) {
            responseSender.sendErrorResponse(ResponseStatusCode.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            errorResponse(responseSender, e);
        }
    }

    private void errorResponse(ResponseSender responseSender, RuntimeException e) {
        LOG.error("Caught exception for " + clazz.getName(), e);
        new HttpErrorHandler(responseSender).errorResponse("Unexpected error");
    }

    static class RequestHandlerInstantationException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RequestHandlerInstantationException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    static class RequestHandlerAccessException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RequestHandlerAccessException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
