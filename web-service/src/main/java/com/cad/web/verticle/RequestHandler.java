package com.cad.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class RequestHandler implements Handler<HttpServerRequest>, ViewCallback, ErrorCallback {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);
    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_POST = "POST";

    private VertxWrapper vertx;
    private ResponseSender responseSender;

    public void setVertx(VertxWrapper vertx) {
        this.vertx = vertx;
    }

    protected VertxWrapper getVertx() {
        return vertx;
    }

    protected ResponseSender getResponseSender() {
        return responseSender;
    }

    protected void setResponseSender(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @Override
    public abstract void handle(HttpServerRequest request);

    @Override
    public void view(String view) {
        sendOkResponse(view);
    }

    @Override
    public void error(ErrorResponse errorResponse, String message) {
        LOG.error("Server returning error response [{}] with message [{}]", errorResponse.name(), message);
        sendErrorResponse(errorResponse, message);
    }

    private void sendOkResponse(String message) {
        getResponseSender().sendResponse(ResponseStatusCode.OK, message);
    }

    private void sendErrorResponse(ErrorCallback.ErrorResponse errorResponse, String message) {
        switch (errorResponse) {
            case CONFLICT:
                getResponseSender().sendErrorResponse(ResponseStatusCode.CONFLICT, message);
                break;
            case NOT_ALLOWED:
                getResponseSender().sendErrorResponse(ResponseStatusCode.FORBIDDEN, message);
                break;
            case NOT_FOUND:
                getResponseSender().sendErrorResponse(ResponseStatusCode.NOT_FOUND, message);
                break;
            case ILLEGAL:
                getResponseSender().sendErrorResponse(ResponseStatusCode.BAD_REQUEST, message);
                break;
            case INTERNAL_ERROR:
                getResponseSender().sendErrorResponse(ResponseStatusCode.INTERNAL_SERVER_ERROR, message);
                break;
            default:
                getResponseSender().sendErrorResponse(ResponseStatusCode.INTERNAL_SERVER_ERROR, message);
                break;
        }
    }

    protected final String getRequestParameter(HttpServerRequest request, final String name) {
        return getRequestParameter(request, name, true);
    }


    protected final String getMandatoryRequestParameter(HttpServerRequest request, String name) {
        String paramValue = getRequestParameter(request, name);
        if (paramValue == null) {
            throw new RequestParameterMissing(name);
        }
        return paramValue;
    }

    protected final String getMandatoryRequestParameter(HttpServerRequest request, String name, boolean doUrlDecode) {
        String paramValue = getRequestParameter(request, name, doUrlDecode);
        if (paramValue == null) {
            throw new RequestParameterMissing(name);
        }
        return paramValue;
    }

    private String getRequestParameter(HttpServerRequest request, final String name, boolean doUrlDecode) {
        if (request == null || request.params() == null || request.params().get(name) == null) {
            return null;
        }
        String result = null;
        try {
            result = doUrlDecode ? URLDecoder.decode(request.params().get(name), "UTF-8") : request.params().get(name);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unable to decode request parameter: {}", e.getMessage());
        }
        return result;
    }

    protected String getMandatoryHeaderParameter(HttpServerRequest request, final String name) {
        String paramValue = getHeaderParameter(request, name);
        if (paramValue == null) {
            throw new HeaderParameterMissing(name);
        }
        return paramValue;
    }

    protected String getMandatoryParameter(JsonObject postObject, final String name) {
        String requestValue = postObject.getString(name);
        if (requestValue == null) {
            throw new RequestParameterMissing(name);
        }
        return requestValue;
    }

    protected String getHeaderParameter(HttpServerRequest request, final String name) {
        if (request == null || request.headers() == null || request.headers().get(name) == null) {
            return null;
        }
        return request.headers().get(name);
    }


    protected boolean isGet(HttpServerRequest request) {
        return REQUEST_METHOD_GET.equals(request.method());
    }

    protected boolean isPost(HttpServerRequest request) {
        return REQUEST_METHOD_POST.equals(request.method());
    }

    protected JsonObject getContainerConfig() {
        return getVertx().getContainerConfig();
    }

    public static class RequestParameterMissing extends RuntimeException {
        public RequestParameterMissing(String message) {
            super(message);
        }
    }

    public static class HeaderParameterMissing extends RuntimeException {
        public HeaderParameterMissing(String message) {
            super(message);
        }
    }
}
