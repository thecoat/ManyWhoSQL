package com.manywho.services.sql.entities;

import com.manywho.sdk.api.ContentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This object contains the metadata information about a table, it is only used internally
 *
 */
public class TableMetadata {

    private String tableName;
    private HashMap<String, ContentType> columns;
    private List<String> columnNames;
    private HashMap<String, String> columnsDatabaseType;
    private List<String> primaryKeyName;
    private String schemaName;
    private HashMap<String, Boolean> propertyAutoincrement;

    public TableMetadata(String tableName, String schemaName){
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.columns = new HashMap<>();
        this.primaryKeyName = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.columnsDatabaseType = new HashMap<>();
        this.propertyAutoincrement = new HashMap<>();
    }

    public void setColumn(String columnName, ContentType columnType, Boolean autoincrement) {
        columns.put(columnName, columnType);
        columnNames.add(columnName);

        if (autoincrement) {
            propertyAutoincrement.put(columnName, true);
        }else {
            propertyAutoincrement.put(columnName, false);
        }

    }
    public void setColumnsDatabaseType(String columnName, String columnType) {
        columnsDatabaseType.put(columnName, columnType);
    }

    public Boolean isColumnAutoincrement (String columnName) {
        return propertyAutoincrement.get(columnName);
    }

    public String getTableName() {
        return tableName;
    }

    public HashMap<String, ContentType> getColumns() {
        return columns;
    }

    public HashMap<String, String> getColumnsDatabaseType() {
        return columnsDatabaseType;
    }

    public List<String> getPrimaryKeyNames() {
        return primaryKeyName;
    }

    public void setPrimaryKeyNames(List<String> primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public List<String> getColumnNames() {return columnNames;}
}
