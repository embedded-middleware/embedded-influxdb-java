package io.github.embedded.influxdb.core;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class InfluxdbHandler {
    private final EmbeddedInfluxdbConfig embeddedInfluxdbConfig;

    public InfluxdbHandler(EmbeddedInfluxdbConfig embeddedInfluxdbConfig) {
        this.embeddedInfluxdbConfig = embeddedInfluxdbConfig;
    }

    public void helloHandler(RoutingContext context) {
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "text/plain");
        response.end("Hello from Influxdb!");
    }
}
