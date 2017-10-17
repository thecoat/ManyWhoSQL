package com.manywho.services.sql.drivers.connection;

import com.google.common.base.Strings;
import com.manywho.services.sql.ServiceConfiguration;

public abstract class BaseDriverConfiguration {

    ServiceConfiguration serviceConfiguration;

    BaseDriverConfiguration(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    abstract String getDriverClass();
    abstract String getDatabaseType();
    abstract String getNoSslConnectionString();
    abstract String getSslConnectionString();
    abstract String getSslCertificateConnectionString();

    public String getStringConnection() {
        checkDriverClass();

        if (serviceConfiguration.getNoUseSsl()) {
            return this.getNoSslConnectionString();
        } else if (Strings.isNullOrEmpty(serviceConfiguration.getServerPublicCertificate())) {
            return this.getSslConnectionString();
        } else {
            return this.getSslCertificateConnectionString();
        }
    }

    private void checkDriverClass() {
        try {
            Class.forName(getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("driver for database type " + getDatabaseType() + "not found");
        }
    }
}
