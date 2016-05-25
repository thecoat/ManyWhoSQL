package com.manywho.services.sql.managers;

import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.MetadataService;

import javax.inject.Inject;
import java.util.List;

public class MetadataManager {
    private ConnectionService connectionService;
    private MetadataService metadataService;

    @Inject
    public MetadataManager(ConnectionService connectionService, MetadataService metadataService){
        this.connectionService = connectionService;
        this.metadataService = metadataService;
    }

    public List<TableMetadata> getMetadataTables(ServiceConfiguration serviceConfiguration) throws Exception {
        return metadataService.getTablesMetadata(
                serviceConfiguration.getDatabaseName(),
                connectionService.getConnection(serviceConfiguration).getMetaData()
        );
    }

    public TableMetadata getMetadataTable(ServiceConfiguration serviceConfiguration, String tableName) throws Exception {

        List<TableMetadata> tableMetadataList = metadataService.getTablesMetadata(
                serviceConfiguration.getDatabaseName(),
                connectionService.getConnection(serviceConfiguration).getMetaData(),
                tableName
        );

        if(tableMetadataList.size() == 1) {

            return tableMetadataList.get(0);
        } else {

            throw new Exception ("table" + tableName + "not found");
        }

    }
}
