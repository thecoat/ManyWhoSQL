package com.manywho.services.sql;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ServiceConfiguration implements Configuration {
    @Configuration.Setting(name="Username", contentType= ContentType.String)
    private String username;

    @Configuration.Setting(name="Password", contentType= ContentType.Password)
    private String password;

    @Configuration.Setting(name="Host", contentType= ContentType.String)
    private String host;

    @Configuration.Setting(name="Port", contentType= ContentType.Number)
    private Integer port;

    @Configuration.Setting(name="Database Type", contentType= ContentType.String)
    private String databaseType;

    @Configuration.Setting(name="Database Name", contentType= ContentType.String)
    private String databaseName;

    @Configuration.Setting(name="Database Schema", contentType= ContentType.String)
    private String databaseSchema;

    @Configuration.Setting(name="Use SSL", contentType= ContentType.Boolean)
    private boolean useSsl;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseSchema() {
        return databaseSchema;
    }

    public Boolean getUseSsl() {
        return useSsl;
    }
}
