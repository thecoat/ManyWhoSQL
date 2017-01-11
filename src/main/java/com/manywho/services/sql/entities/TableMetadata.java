package com.manywho.services.sql.entities;

import com.google.common.base.Strings;
import com.manywho.sdk.api.ContentType;
import com.manywho.services.sql.utilities.ManyWhoSpecialComment;

import java.util.*;

/**
 * This object contains the metadata information about a table, it is only used internally
 *
 */
public class TableMetadata {

    private String tableName;
    private String tableNameAlias;
    private HashMap<String, ContentType> columns;
    private HashMap<String, String> aliases;
    private List<String> columnNames;
    private HashMap<String, String> columnsDatabaseType;
    private List<String> primaryKeyName;
    private String schemaName;
    private HashMap<String, Boolean> propertyAutoincrement;

    public TableMetadata(String tableName, String tableNameAlias, String schemaName){
        this.tableName = tableName;
        this.tableNameAlias = tableNameAlias;
        this.schemaName = schemaName;
        this.columns = new HashMap<>();
        this.aliases = new HashMap<>();
        this.primaryKeyName = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.columnsDatabaseType = new HashMap<>();
        this.propertyAutoincrement = new HashMap<>();
    }

    public void setColumn(String columnName, ContentType columnType, Boolean autoincrement, String remarks) {
        columns.put(columnName, columnType);
        columnNames.add(columnName);
        aliases.put(columnName, ManyWhoSpecialComment.nameComment(remarks));

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

    /**
     * Return the column name and ContentType
     * @return
     */
    public HashMap<String, ContentType> getColumnsAndContentTypeWithAlias() {
        HashMap <String, ContentType> response = new HashMap<>();
        for (Map.Entry<String, ContentType> property:columns.entrySet()) {
            if (aliases.get(property.getKey())!= null) {
                response.put(aliases.get(property.getKey()), property.getValue());
            } else {
                response.put(property.getKey(), property.getValue());
            }
        }

        return response;
    }

    public HashMap<String, String> getColumnsDatabaseType() {
        return columnsDatabaseType;
    }

    public String getColumnDatabaseType(String nameOrAlias) {
        String name = nameOrAlias;

        if (isAlias(nameOrAlias)) {
            return columnsDatabaseType.get(getColumnNameOrAlias(nameOrAlias));
        }

        return columnsDatabaseType.get(name);
    }

    public String getColumnNameOrAlias(String nameOrAlias) {
        if (columns.get(nameOrAlias) == null) {
            //is alias

            Optional<Map.Entry<String, String>> first = aliases.entrySet().stream()
                    .filter(c -> c.getValue() != null && Objects.equals(c.getValue(), nameOrAlias))
                    .findFirst();

            if (first.isPresent()) {
                return first.get().getKey();
            }

            throw new RuntimeException("Name of Column not found");
        }

        //is name
        return nameOrAlias;
    }

    public String getColumnAliasOrName(String aliasOrName) {
        if (columns.get(aliasOrName)== null) {
            // it is not a column
            Optional<Map.Entry<String, String>> alias = aliases.entrySet().stream().filter(p -> Objects.equals(p.getValue(), aliasOrName)).findFirst();

            if (alias.isPresent()) {
                return alias.get().getValue();
            }

        } else if (columns.get(aliasOrName)!= null){
            if (aliases.get(aliasOrName) != null) {
                // it is a column and exist an alias for it
                return aliases.get(aliasOrName);
            }

            // it is a column but there isn't alias for it
            return aliasOrName;
        }

        throw new RuntimeException("Name of Column not found");
    }

    private boolean isAlias(String columnName) {
        return columns.get(columnName) == null;
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
