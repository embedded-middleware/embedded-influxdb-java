package io.github.embedded.influxdb.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class EmbeddedInfluxdbServer {
    private final EmbeddedInfluxdbConfig embeddedInfluxdbConfig;

    private final InfluxdbHandler influxdbHandler;

    private final Vertx vertx;

    private int listeningPort = -1;

    public EmbeddedInfluxdbServer() {
        this(new EmbeddedInfluxdbConfig());
    }

    public EmbeddedInfluxdbServer(EmbeddedInfluxdbConfig embeddedInfluxdbConfig) {
        this.embeddedInfluxdbConfig = embeddedInfluxdbConfig;
        this.influxdbHandler = new InfluxdbHandler(embeddedInfluxdbConfig);
        this.vertx = Vertx.vertx();
    }

    public CompletableFuture<Void> start() {
        CompletableFuture<Void> startFuture = new CompletableFuture<>();
        vertx.deployVerticle(new AbstractVerticle() {
            @Override
            public void start() {
                Router router = Router.router(vertx);
                router.get("/").handler(influxdbHandler::helloHandler);

                HttpServerOptions options = new HttpServerOptions();
                if (embeddedInfluxdbConfig.isUseSsl()) {
                    options.setSsl(true);
                    options.setClientAuth(ClientAuth.REQUIRED);
                    JksOptions keyStoreOptions = new JksOptions();
                    keyStoreOptions.setPath(embeddedInfluxdbConfig.getKeyStorePath());
                    keyStoreOptions.setPassword(embeddedInfluxdbConfig.getKeyStorePassword());
                    options.setKeyStoreOptions(keyStoreOptions);
                    JksOptions trustStoreOptions = new JksOptions();
                    trustStoreOptions.setPath(embeddedInfluxdbConfig.getTrustStorePath());
                    trustStoreOptions.setPassword(embeddedInfluxdbConfig.getTrustStorePassword());
                    options.setTrustStoreOptions(trustStoreOptions);
                }

                HttpServer httpServer = vertx.createHttpServer(options);
                httpServer.requestHandler(router);
                httpServer.listen(embeddedInfluxdbConfig.getPort(), res -> {
                    if (res.succeeded()) {
                        listeningPort = res.result().actualPort();
                        startFuture.complete(null);
                        log.info("Embedded InfluxDB Server started on port {}", listeningPort);
                    } else {
                        startFuture.completeExceptionally(res.cause());
                    }
                });
            }
        });
        return startFuture;
    }

    public int getPort() {
        return listeningPort;
    }

    public void startSync() throws ExecutionException, InterruptedException {
        start().get();
    }

    public CompletableFuture<Void> close() {
        CompletableFuture<Void> closeFuture = new CompletableFuture<>();
        vertx.close(res -> {
            if (res.succeeded()) {
                closeFuture.complete(null);
                log.info("Embedded InfluxDB Server stopped");
            } else {
                closeFuture.completeExceptionally(res.cause());
            }
        });
        return closeFuture;
    }

    public void closeSync() throws ExecutionException, InterruptedException {
        close().get();
    }
}
