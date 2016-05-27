package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.DataService;

import javax.inject.Inject;
import java.util.List;

public class DataManager {

    private ConnectionService connectionService;
    private DataService dataService;
    private MetadataManager metadataManager;

    @Inject
    public DataManager(ConnectionService connectionService, DataService dataService, MetadataManager metadataManager){
        this.connectionService = connectionService;
        this.dataService = dataService;
        this.metadataManager = metadataManager;
    }

    public List<MObject> load(ServiceConfiguration configuration, ObjectDataType objectDataType, String id) throws Exception {

        return dataService.getTableContentByPrimaryKey(
                metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName()),
                        connectionService.getConnection(configuration),
                        id
                    );
    }

    public List<MObject> loadBySearch(ServiceConfiguration configuration, ObjectDataType objectDataType, String search) throws Exception {

        return dataService.getTableContentBySearch(
                metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName()),
                connectionService.getConnection(configuration),
                search
        );
    }

    public MObject update(ServiceConfiguration configuration, MObject mObject) throws Exception {

        dataService.update(
                mObject,
                connectionService.getConnection(configuration),
                metadataManager.getMetadataTable(configuration, mObject.getDeveloperName())
        );

        return mObject;
    }
}
