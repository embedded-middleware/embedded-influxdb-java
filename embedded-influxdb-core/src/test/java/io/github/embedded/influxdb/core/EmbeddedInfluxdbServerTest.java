package io.github.embedded.influxdb.core;

import org.junit.jupiter.api.Test;

public class EmbeddedInfluxdbServerTest {
    @Test
    public void testInfluxdbServerBoot() throws Exception {
        EmbeddedInfluxdbServer server = new EmbeddedInfluxdbServer();
        server.start();
        server.close();
    }
}
