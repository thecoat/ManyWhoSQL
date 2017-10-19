package com.manywho.services.sql.managers;

import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.MetadataService;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import java.sql.DatabaseMetaData;
import java.util.List;

public class MetadataManager {
    private ConnectionManager connectionManager;
    private MetadataService metadataService;

    @Inject
    public MetadataManager(ConnectionManager connectionManager, MetadataService metadataService){
        this.connectionManager = connectionManager;
        this.metadataService = metadataService;
    }

    public List<TableMetadata> getMetadataTables(Sql2o sql2o, ServiceConfiguration serviceConfiguration) throws Exception {

        try (Connection con = sql2o.open()) {
            DatabaseMetaData metaData = con.getJdbcConnection().getMetaData();

            return metadataService.getTablesMetadata(
                    serviceConfiguration.getDatabaseName(),
                    serviceConfiguration.getDatabaseSchema(),
                    metaData
            );
        }
    }

    public TableMetadata getMetadataTable(ServiceConfiguration serviceConfiguration, String tableName) throws Exception {

        try (Connection con = connectionManager.getSql2Object(serviceConfiguration).open()) {
            DatabaseMetaData metaData = con.getJdbcConnection().getMetaData();

            List<TableMetadata> tableMetadataLis = metadataService.getTablesMetadata(
                    serviceConfiguration.getDatabaseName(),
                    serviceConfiguration.getDatabaseSchema(),
                    metaData,
                    tableName
            );

            if(tableMetadataLis.size() == 1) {

                return tableMetadataLis.get(0);
            } else {

                throw new Exception ("table" + tableName + "not found");
            }
        }
    }
}
