package nl.cad.starter;

import org.slf4j.LoggerFactory;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.platform.Verticle;


public class Starter extends Verticle{

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Starter.class);

    @Override
    public void start() {

        LOG.info("Starting starter app");

        container.deployModule("com.cad.ms~web-service~1.0.0-final", new AsyncResultHandler<String>() {
            public void handle(AsyncResult<String> asyncResult) {
                if (asyncResult.succeeded()) {
                    System.out.println("The web service module has been deployed, deployment ID is " + asyncResult.result());
                } else {
                    asyncResult.cause().printStackTrace();
                }
            }
        });

        container.deployModule("com.cad.ms~course-service~1.0.0-final", new AsyncResultHandler<String>() {
            public void handle(AsyncResult<String> asyncResult) {
                if (asyncResult.succeeded()) {
                    System.out.println("The course service module has been deployed, deployment ID is " + asyncResult.result());
                } else {
                    asyncResult.cause().printStackTrace();
                }
            }
        });
    }
}
