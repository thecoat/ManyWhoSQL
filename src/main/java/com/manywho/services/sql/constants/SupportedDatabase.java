package com.manywho.services.sql.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SupportedDatabase {
    MYSQL("mysql"),
    POSTGRESSQL("postgresql"),
    SQLSERVER("sqlserver");

    private final String text;

    private SupportedDatabase(String text) {
        this.text = text;
    }

    @JsonCreator
    public static SupportedDatabase forValue(String value) {
        SupportedDatabase[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            SupportedDatabase supportedDatabase = var1[var3];
            if (value.equalsIgnoreCase(supportedDatabase.text)) {
                return supportedDatabase;
            }
        }

        throw new IllegalArgumentException("No supported database type with text " + value + " found");
    }

    public String toString() {
        return this.text;
    }
}
