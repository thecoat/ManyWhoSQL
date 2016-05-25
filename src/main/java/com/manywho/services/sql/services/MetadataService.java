package com.manywho.services.sql.services;

import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.utilities.ContentTypeUtil;

import javax.inject.Inject;
import java.sql.DatabaseMetaData;
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

        return this.getTablesPrivate(databaseName, schemaPattern, tableNamePattern, types, metaData);
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, DatabaseMetaData metaData) throws Exception {
        String catalog = databaseName;
        String schemaPattern = null;
        String tableNamePattern = null;
        String[] types = {"TABLE"};

        return this.getTablesPrivate(databaseName, schemaPattern, tableNamePattern, types, metaData);
    }

    public List<TableMetadata> getTablesPrivate(String catalog, String schemaPattern, String tableNamePattern, String[] types, DatabaseMetaData metaData) throws Exception {

        ResultSet rsTablesMetadata = metaData.getTables(catalog, schemaPattern, tableNamePattern, types );
        List<TableMetadata> tableList = new ArrayList<>();

        while(rsTablesMetadata.next()) {
            TableMetadata tableMetadata = new TableMetadata(rsTablesMetadata.getString(3));
            tableMetadata.setPrimaryKeyName(rsTablesMetadata.getString(4));
            populateColumnForTable(catalog, tableMetadata, metaData);
            tableList.add(tableMetadata);
        }

        return tableList;
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
        }
    }
}
