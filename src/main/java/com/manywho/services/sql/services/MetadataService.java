package com.manywho.services.sql.services;

import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;
import com.manywho.services.sql.utilities.ContentTypeUtil;

import javax.inject.Inject;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MetadataService {

    @Inject
    public MetadataService(){
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, String schemaName, DatabaseMetaData metaData) throws Exception {
        String[] types = {"TABLE"};

        return this.getTablesMetadataInternal(databaseName, schemaName, null, types, metaData);
    }

    public List<TableMetadata> getTablesMetadata(String databaseName, String schemaName, DatabaseMetaData metaData, String tableName) throws Exception {
        String[] types = {"TABLE"};

        return this.getTablesMetadataInternal(databaseName, schemaName, tableName, types, metaData);
    }

    private List<TableMetadata> getTablesMetadataInternal(String catalog, String schemaName, String tableNamePattern, String[] types, DatabaseMetaData metaData) throws Exception {
        ResultSet rsTablesMetadata = metaData.getTables(catalog, schemaName, tableNamePattern, types );
        List<TableMetadata> tableList = new ArrayList<>();

        while(rsTablesMetadata.next()) {
            TableMetadata tableMetadata = new TableMetadata(rsTablesMetadata.getString(3), schemaName);
            populateColumnForTable(catalog, schemaName, tableMetadata, metaData);
            populatePrimaryKeyForTable(catalog, schemaName, tableMetadata, metaData);
            tableList.add(tableMetadata);
        }

        return tableList;
    }

    private void populatePrimaryKeyForTable(String databaseName, String databaseSchema, TableMetadata tableMetadata, DatabaseMetaData metaData) throws Exception {
        ResultSet rsPrimaryKey = metaData.getPrimaryKeys(databaseName, databaseSchema, tableMetadata.getTableName());
        ArrayList<String> primaryKeys = new ArrayList<>();

        while (rsPrimaryKey.next()) {
            primaryKeys.add(rsPrimaryKey.getString("COLUMN_NAME"));
        }

        tableMetadata.setPrimaryKeyNames(primaryKeys);
    }

    private void populateColumnForTable(String databaseName, String databaseSchema, TableMetadata tableMetadata, DatabaseMetaData metaData) throws SQLException {
        ResultSet rsColumnsMetadata = metaData.getColumns(databaseName, databaseSchema, tableMetadata.getTableName(), null);

        while (rsColumnsMetadata.next()) {
            try {
                tableMetadata.setColumn(
                        rsColumnsMetadata.getString(4),
                        ContentTypeUtil.createFromSqlType(rsColumnsMetadata.getInt(5), rsColumnsMetadata.getString(6)),
                        rsColumnsMetadata.getString(6)
                );

                tableMetadata.setColumnsDatabaseType(
                        rsColumnsMetadata.getString(4),
                        addDatabaseSpecificTypes(rsColumnsMetadata.getInt(5),rsColumnsMetadata.getString(6) )
                );

            } catch (DataBaseTypeNotSupported e) {
                // if some type is not supported we just ignore it
            }
        }
    }

    private String addDatabaseSpecificTypes(int type, String databaseSpecificType) {
        switch(type){
            case ContentTypeUtil.SQL_SERVER_DATETIMEOFFSET:
                return ContentTypeUtil.SQL_SERVER_dATETIMEOFFSET_TEXT;
            case ContentTypeUtil.POSTGRESQL_UUID:
                if (Objects.equals(databaseSpecificType, ContentTypeUtil.POSTGRESQL_UUID_TEXT)) {
                    return ContentTypeUtil.POSTGRESQL_UUID_TEXT;
                } else {
                    return JDBCType.valueOf(type).getName();
                }
            default:
                return JDBCType.valueOf(type).getName();
        }
    }
}
