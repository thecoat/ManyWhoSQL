package com.manywho.services.sql.managers;

import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.drivers.connection.MySqlDriverConfiguration;
import com.manywho.services.sql.drivers.connection.PostgreSqlDriverConfiguration;
import com.manywho.services.sql.drivers.connection.SqlServerDriverConfiguration;
import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.PostgresQuirks;

public class ConnectionManager {

    private Sql2o sql2o;

    public ConnectionManager() {
        sql2o = null;
    }

    Sql2o getSql2Object(ServiceConfiguration serviceConfiguration) throws Exception {

        if(sql2o!= null) {
            return sql2o;
        }

        switch (serviceConfiguration.getDatabaseType()) {
            case "mysql":
                return new Sql2o(
                        new MySqlDriverConfiguration(serviceConfiguration).getStringConnection(),
                        serviceConfiguration.getUsername(),
                        serviceConfiguration.getPassword(),
                        new NoQuirks());
            case "sqlserver":
                return new Sql2o(
                        new SqlServerDriverConfiguration(serviceConfiguration).getStringConnection(),
                        serviceConfiguration.getUsername(),
                        serviceConfiguration.getPassword(),
                        new NoQuirks());
            case "postgresql":
                return new Sql2o(
                        new PostgreSqlDriverConfiguration(serviceConfiguration).getStringConnection(),
                        serviceConfiguration.getUsername(),
                        serviceConfiguration.getPassword(),
                        new PostgresQuirks());
            default:
                throw new RuntimeException(String.format("database %s not supported", serviceConfiguration.getDatabaseType()));
        }
    }
}
