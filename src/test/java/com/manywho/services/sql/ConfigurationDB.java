package com.manywho.services.sql;

import com.manywho.services.sql.utilities.ServiceConfigurationProperties;

public class ConfigurationDB {
    static public String portForTest;
    static public String databaseTypeForTest;
    static public String databaseNameForTest;
    static public String schemaForTest ;
    static public String hostForTest;

    static public void setPorperties(String databasetType) {
        ServiceConfigurationProperties serviceConfigurationProperties = new ServiceConfigurationProperties();

        ConfigurationDB.portForTest = serviceConfigurationProperties.get(databasetType + "_port");
        ConfigurationDB.databaseTypeForTest = serviceConfigurationProperties.get(databasetType + "_databaseType");
        ConfigurationDB.schemaForTest = serviceConfigurationProperties.get(databasetType + "_schema");
        ConfigurationDB.hostForTest = serviceConfigurationProperties.get(databasetType + "_host");
        ConfigurationDB.databaseNameForTest = serviceConfigurationProperties.get(databasetType + "_databaseName");
    }
}
