package com.cad.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;


public class WebServiceVerticle extends Verticle {

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceVerticle.class);
    public static final String HTTP_PORT = "http_port";
    private static final String HOST = "localhost";

    @Override
    public void start() {
        LOG.info("WebServiceVerticle online. Listening on port [" + getHttpPortFromConfig() + "]");
        System.out.println("WebServiceVerticle online. Listening on port [" + getHttpPortFromConfig() + "]");

        VertxWrapper vertxWrapper = createVertxWrapper();

        HttpServer httpServer = vertx.createHttpServer();
        WebRouter router = new WebRouter(new RouteMatcher(), vertxWrapper);
        httpServer.requestHandler(router);
        httpServer.listen(getHttpPortFromConfig(), HOST);
    }

    private VertxWrapper createVertxWrapper() {
        VertxWrapper vertxWrapper = new VertxWrapper(vertx);
        vertxWrapper.setContainerConfig(container.config());
        return vertxWrapper;
    }

    private int getHttpPortFromConfig() {
        return 8899;// container.config().getInteger(HTTP_PORT);
    }

}
