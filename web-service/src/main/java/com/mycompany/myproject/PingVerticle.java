package com.mycompany.myproject;


import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/*
 * This is a simple Java verticle which receives `ping` messages on the event bus and sends back `pong` replies
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class PingVerticle extends Verticle {

  public void start() {

    final Logger logger = container.logger();



    logger.info("PingVerticle started");

    JsonObject conf = new JsonObject().putString("web_root", "webroot").putNumber("port", 8080);

    container.deployModule("io.vertx~mod-web-server~2.0.0-final", conf);

  }
}
