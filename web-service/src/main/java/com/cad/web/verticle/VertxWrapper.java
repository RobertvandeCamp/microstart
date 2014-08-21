package com.cad.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

public class VertxWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(VertxWrapper.class);
    private final Vertx vertx;
    private JsonObject containerConfig;

    public VertxWrapper(Vertx vertx) {
        this.vertx = vertx;
    }

    public void send(String address, JsonObject json, Handler<Message<JsonObject>> handler) {
        vertx.eventBus().send(address, json, handler);
    }

    public JsonObject getContainerConfig() {
        return containerConfig;
    }

    public void setContainerConfig(JsonObject containerConfig) {
        this.containerConfig = containerConfig;
    }

    public <T> ConcurrentMap<String, T> getSharedMap(String mapName) {
        return getWrappedVertx().sharedData().getMap(mapName);
    }

    public Vertx getWrappedVertx() {
        return vertx;
    }
}