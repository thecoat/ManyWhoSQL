package com.manywho.services.sql.entities;

public enum DatabaseType {
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),
    SQL_SERVER("sqlserver");

    private String type;

    DatabaseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static DatabaseType forValue(String value) {
        switch (value){
            case "mysql":
                return MYSQL;
            case "postgresql":
                return POSTGRESQL;
            case "sqlserver":
                return SQL_SERVER;
            default:
                throw new RuntimeException(String.format("Database type \"%s\" not supported.", value));
        }
    }
}
