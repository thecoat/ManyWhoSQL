package com.manywho.services.sql.drivers.connection;

import com.manywho.services.sql.ServiceConfiguration;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PostgreSqlDriverConfiguration extends BaseDriverConfiguration {

    public PostgreSqlDriverConfiguration(ServiceConfiguration serviceConfiguration) {
        super(serviceConfiguration);
    }


    @Override
    String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    String getDatabaseType() {
        return "postgresql";
    }

    @Override
    String getNoSslConnectionString() {
        return String.format("jdbc:postgresql://%s:%s/%s",
                serviceConfiguration.getHost(),
                serviceConfiguration.getPort(),
                serviceConfiguration.getDatabaseName());
    }

    @Override
    String getSslConnectionString() {
        return String.format("jdbc:postgresql://%s:%s/%s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
                serviceConfiguration.getHost(),
                serviceConfiguration.getPort(),
                serviceConfiguration.getDatabaseName());
    }

    @Override
    String getSslCertificateConnectionString() {
        try {
            String certificate = URLEncoder.encode(serviceConfiguration.getServerPublicCertificate(), "UTF-8");

            return String.format("jdbc:postgresql://%s:%s/%s?ssl=true&sslmode=verify-full&sslfactory=org.postgresql.ssl.SingleCertValidatingFactory&sslfactoryarg=%s",
                    serviceConfiguration.getHost(),
                    serviceConfiguration.getPort(),
                    serviceConfiguration.getDatabaseName(),
                    certificate);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
