package com.manywho.services.sql.managers;

import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.drivers.connection.DriverConfigurationInterface;
import com.manywho.services.sql.drivers.connection.MySqlDriverConfiguration;
import com.manywho.services.sql.drivers.connection.PostgreSqlDriverConfiguration;
import com.manywho.services.sql.drivers.connection.SqlServerDriverConfiguration;
import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.PostgresQuirks;
import org.sql2o.quirks.Quirks;

public class ConnectionManager {

    public static Sql2o getSql2Object(ServiceConfiguration serviceConfiguration) {

        switch (serviceConfiguration.getDatabaseType()) {
            case "mysql":
                return createSql2o(new MySqlDriverConfiguration(), serviceConfiguration, new NoQuirks());
            case "sqlserver":
                return createSql2o(new SqlServerDriverConfiguration(), serviceConfiguration, new NoQuirks());
            case "postgresql":
                return createSql2o(new PostgreSqlDriverConfiguration(), serviceConfiguration, new PostgresQuirks());
            default:
                throw new RuntimeException(String.format("The database type \"%s\" is not supported",
                        serviceConfiguration.getDatabaseType()));
        }
    }

    private static Sql2o createSql2o(DriverConfigurationInterface driverConfiguration,
                              ServiceConfiguration serviceConfiguration, Quirks quirks) {
        try {
            Class.forName(driverConfiguration.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("A driver for the database type \"%s\" could not be found ",
                    serviceConfiguration.getDatabaseType()));
        }

        return new Sql2o(
                driverConfiguration.getStringConnection(serviceConfiguration),
                serviceConfiguration.getUsername(),
                serviceConfiguration.getPassword(),
                quirks);
    }
}
