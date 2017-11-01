package com.manywho.services.sql.entities;

public enum DatabaseType {
    Mysql("mysql"),
    Postgresql("postgresql"),
    Sqlserver("sqlserver");

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
                return Mysql;
            case "postgresql":
                return Postgresql;
            case "sqlserver":
                return Sqlserver;
            default:
                throw new RuntimeException(String.format("Database type \"%s\" not supported.", value));
        }
    }
}
