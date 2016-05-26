package com.manywho.services.sql.services;

import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.utilities.ContentTypeUtil;

import javax.inject.Inject;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MetadataService {

    @Inject
    public MetadataService(){
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, DatabaseMetaData metaData, String tableNamePattern) throws Exception {
        String catalog = databaseName;
        String schemaPattern = null;
        String[] types = {"TABLE"};

        return this.getTablesMetadatGeneric(databaseName, schemaPattern, tableNamePattern, types, metaData);
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, DatabaseMetaData metaData) throws Exception {
        String catalog = databaseName;
        String schemaPattern = null;
        String tableNamePattern = null;
        String[] types = {"TABLE"};

        return this.getTablesMetadatGeneric(databaseName, schemaPattern, tableNamePattern, types, metaData);
    }

    private List<TableMetadata> getTablesMetadatGeneric(String catalog, String schemaPattern, String tableNamePattern, String[] types, DatabaseMetaData metaData) throws Exception {

        ResultSet rsTablesMetadata = metaData.getTables(catalog, schemaPattern, tableNamePattern, types );
        List<TableMetadata> tableList = new ArrayList<>();

        while(rsTablesMetadata.next()) {
            TableMetadata tableMetadata = new TableMetadata(rsTablesMetadata.getString(3));

            populateColumnForTable(catalog, tableMetadata, metaData);
            populatePrimaryKeyForTable(catalog, tableMetadata, metaData);
            tableList.add(tableMetadata);

        }

        return tableList;
    }

    private void populatePrimaryKeyForTable(String databaseName, TableMetadata tableMetadata, DatabaseMetaData metaData) throws Exception {
        String catalog           = databaseName;
        String schemaPattern     = null;
        String tableNamePattern  = tableMetadata.getTableName();

        ResultSet rsPrimaryKey = metaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern);

        while (rsPrimaryKey.next()) {
            tableMetadata.setPrimaryKeyName(rsPrimaryKey.getString("COLUMN_NAME"));
        }
    }

    private void populateColumnForTable(String databaseName, TableMetadata tableMetadata, DatabaseMetaData metaData) throws Exception {
        String catalog           = databaseName;
        String schemaPattern     = null;
        String tableNamePattern  = tableMetadata.getTableName();
        String columnNamePattern = null;

        ResultSet rsCollumnsMetadata = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);

        while (rsCollumnsMetadata.next()) {

            tableMetadata.setColumn(
                    rsCollumnsMetadata.getString(4),
                    ContentTypeUtil.createFromSqlType(rsCollumnsMetadata.getInt(5))
            );

            tableMetadata.setColumnsDatabaseType(
                    rsCollumnsMetadata.getString(4),
                    JDBCType.valueOf(rsCollumnsMetadata.getInt(5)).getName()
            );
        }
    }
}
