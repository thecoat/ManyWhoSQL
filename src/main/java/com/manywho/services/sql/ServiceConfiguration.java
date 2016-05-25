package com.manywho.services.sql;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ServiceConfiguration implements Configuration {
    @Configuration.Value(name="Host", contentType= ContentType.String)
    private String host;

    @Configuration.Value(name="Port", contentType= ContentType.Number)
    private Integer port;

    @Configuration.Value(name="Username", contentType= ContentType.String)
    private String username;

    @Configuration.Value(name="Password", contentType= ContentType.Password)
    private String password;

    @Configuration.Value(name="Database Name", contentType= ContentType.String)
    private String databaseName;

    @Configuration.Value(name="Database Type", contentType= ContentType.String)
    private String databaseType;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseType() {
        return databaseType;
    }
}
