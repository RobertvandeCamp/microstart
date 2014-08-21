package com.cad.web.handler;

import com.cad.web.verticle.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

public class GetCoursesHandler extends RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GetCoursesHandler.class);

    @Override
    public void handle(HttpServerRequest request) {
        LOG.debug("Handling request");
        JsonObject body = new JsonObject().putString("provider", "Coursera");
        getVertx().send("courseService", body, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                JsonObject response = event.body();
                System.out.println("received body "+ response.encodePrettily());
                view(response.encodePrettily());
            }
        });
    }
}
