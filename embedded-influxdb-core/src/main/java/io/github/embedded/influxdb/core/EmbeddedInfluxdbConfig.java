package io.github.embedded.influxdb.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class EmbeddedInfluxdbConfig {
    private int port;

    public boolean useSsl;

    public String keyStorePath;

    @ToString.Exclude
    public String keyStorePassword;

    public String trustStorePath;

    @ToString.Exclude
    public String trustStorePassword;

    public EmbeddedInfluxdbConfig() {
    }

    public EmbeddedInfluxdbConfig port(int port) {
        this.port = port;
        return this;
    }

    public EmbeddedInfluxdbConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public EmbeddedInfluxdbConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public EmbeddedInfluxdbConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public EmbeddedInfluxdbConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public EmbeddedInfluxdbConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }
}
