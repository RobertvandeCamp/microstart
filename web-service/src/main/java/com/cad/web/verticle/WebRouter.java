package com.cad.web.verticle;

import com.cad.web.handler.GetCoursesHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;

class WebRouter implements Handler<HttpServerRequest> {

    public static final String API_CONTEXT = "/api";

    private final RouteMatcher routeMatcher;
    private final VertxWrapper vertx;
    private final JsonObject containerConfig;

    public WebRouter(RouteMatcher routeMatcher, VertxWrapper vertx) {
        this.routeMatcher = routeMatcher;
        this.vertx = vertx;
        this.containerConfig = vertx.getContainerConfig();
        registerHandlers();
    }

    @Override
    public void handle(HttpServerRequest event) {
        routeMatcher.handle(event);
    }

    private void registerHandlers() {
        routeMatcher.get(API_CONTEXT + "/courses", new WebRequestHandlerFactory(vertx, new JsonObject(), GetCoursesHandler.class));

        routeMatcher.noMatch(new FourOFourHandler());
    }

}
