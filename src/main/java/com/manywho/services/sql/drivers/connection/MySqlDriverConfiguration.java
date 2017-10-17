package com.manywho.services.sql.drivers.connection;

import com.manywho.services.sql.ServiceConfiguration;

public class MySqlDriverConfiguration extends BaseDriverConfiguration {

    public MySqlDriverConfiguration(ServiceConfiguration serviceConfiguration) {
        super(serviceConfiguration);
    }

    @Override
    String getDriverClass() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    String getDatabaseType() {
        return "mysql";
    }

    @Override
    String getNoSslConnectionString() {
        return String.format("jdbc:mysql://%s:%s/%s",
                serviceConfiguration.getHost(),
                serviceConfiguration.getPort(),
                serviceConfiguration.getDatabaseName());
    }

    @Override
    String getSslConnectionString() {
        return String.format("jdbc:mysql://%s:%s/%s?useSSL=true&trustServerCertificate=true",
                serviceConfiguration.getHost(),
                serviceConfiguration.getPort(),
                serviceConfiguration.getDatabaseName());
    }

    @Override
    String getSslCertificateConnectionString() {
        return String.format("jdbc:mysql://%s:%s/%s?useSSL=true&trustServerCertificate=false&serverSslCert=%s",
                serviceConfiguration.getHost(),
                serviceConfiguration.getPort(),
                serviceConfiguration.getDatabaseName(),
                serviceConfiguration.getServerPublicCertificate());
    }
}
