package com.revolut.transfers.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.*;

@Singleton
public class RestServer {
    private static final Logger LOG = LoggerFactory.getLogger(RestServer.class);
    public static final Integer SERVER_PORT = 8080;

    private final RestRouter router;

    @Inject
    private RestServer(RestRouter router) {
        this.router = router;
    }

    public void start() {
        LOG.info(String.format("Starting REST server on port %d", SERVER_PORT));

        port(SERVER_PORT);

        configureReqResLogging();
        router.configureRoutes();

        awaitInitialization();

        LOG.info("REST server started");
    }

    public void stop() {
        Spark.stop();
        awaitStop();

        LOG.info("REST server stopped");
    }

    private void configureReqResLogging() {
        before("/*", (req, res) ->
                LOG.info(String.format("Received API call: %s %s", req.requestMethod(), req.pathInfo())));
        after("/*", (req, res) ->
                LOG.info(String.format("Handled API call: %s %s with status %d", req.requestMethod(), req.pathInfo(), res.status())));
    }
}
