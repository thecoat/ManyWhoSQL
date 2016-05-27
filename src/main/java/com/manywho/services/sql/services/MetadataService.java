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

    public List<TableMetadata> getTablesMetadata(String databaseName, String databaseSchema, DatabaseMetaData metaData) throws Exception {
        String[] types = {"TABLE"};

        return this.getTablesMetadataInternal(databaseName, databaseSchema, null, types, metaData);
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, String databaseSchema, DatabaseMetaData metaData, String tableName) throws Exception {
        String[] types = {"TABLE"};

        return this.getTablesMetadataInternal(databaseName, databaseSchema, tableName, types, metaData);
    }

    private List<TableMetadata> getTablesMetadataInternal(String catalog, String schemaPattern, String tableNamePattern, String[] types, DatabaseMetaData metaData) throws Exception {
        ResultSet rsTablesMetadata = metaData.getTables(catalog, schemaPattern, tableNamePattern, types );
        List<TableMetadata> tableList = new ArrayList<>();

        while(rsTablesMetadata.next()) {
            TableMetadata tableMetadata = new TableMetadata(rsTablesMetadata.getString(3));
            populateColumnForTable(catalog, schemaPattern, tableMetadata, metaData);
            populatePrimaryKeyForTable(catalog, schemaPattern, tableMetadata, metaData);
            tableList.add(tableMetadata);
        }

        return tableList;
    }

    private void populatePrimaryKeyForTable(String databaseName, String databaseSchema, TableMetadata tableMetadata, DatabaseMetaData metaData) throws Exception {
        ResultSet rsPrimaryKey = metaData.getPrimaryKeys(databaseName, databaseSchema, tableMetadata.getTableName());

        while (rsPrimaryKey.next()) {
            tableMetadata.setPrimaryKeyName(rsPrimaryKey.getString("COLUMN_NAME"));
        }
    }

    private void populateColumnForTable(String databaseName, String databaseSchema, TableMetadata tableMetadata, DatabaseMetaData metaData) throws Exception {
        ResultSet rsColumnsMetadata = metaData.getColumns(databaseName, databaseSchema, tableMetadata.getTableName(), null);

        while (rsColumnsMetadata.next()) {
            tableMetadata.setColumn(
                    rsColumnsMetadata.getString(4),
                    ContentTypeUtil.createFromSqlType(rsColumnsMetadata.getInt(5))
            );

            tableMetadata.setColumnsDatabaseType(
                    rsColumnsMetadata.getString(4),
                    JDBCType.valueOf(rsColumnsMetadata.getInt(5)).getName()
            );
        }
    }
}
