package com.manywho.services.sql.utilities;

import java.util.Properties;

public class ServiceConfigurationProperties {
    protected Properties properties;

    public ServiceConfigurationProperties() {
        try {
            properties = new Properties();

            if(getClass().getClassLoader().getResourceAsStream("configuration.properties") != null) {
                properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));
            } else {
                properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties.dist"));
            }

        } catch (Exception exception) {
            properties = new Properties();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public boolean has(String key) {
        return properties.containsKey(key);
    }
}
