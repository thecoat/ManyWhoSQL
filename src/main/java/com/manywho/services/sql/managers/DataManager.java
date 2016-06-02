package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.DataService;
import com.manywho.services.sql.services.QueryService;

import javax.inject.Inject;
import java.util.List;

public class DataManager {

    private ConnectionService connectionService;
    private DataService dataService;
    private MetadataManager metadataManager;
    private QueryService queryService;

    @Inject
    public DataManager(ConnectionService connectionService, DataService dataService, MetadataManager metadataManager, QueryService queryService){
        this.connectionService = connectionService;
        this.dataService = dataService;
        this.metadataManager = metadataManager;
        this.queryService = queryService;
    }

    public List<MObject> load(ServiceConfiguration configuration, ObjectDataType objectDataType, String id) throws Exception {

        return dataService.fetchByPrimaryKey(
                metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName()),
                        connectionService.getSql2Object(configuration), id);
    }

    public List<MObject> loadBySearch(ServiceConfiguration configuration, ObjectDataType objectDataType, ListFilter filters) throws Exception {

        return dataService.fetchBySearch(
                metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName()),
                connectionService.getSql2Object(configuration),
                queryService.getSqlFromFilter(configuration, objectDataType, filters));
    }

    public MObject update(ServiceConfiguration configuration, MObject mObject) throws Exception {

        dataService.update(mObject, connectionService.getSql2Object(configuration),
                metadataManager.getMetadataTable(configuration, mObject.getDeveloperName()));

        return mObject;
    }

    public MObject create(ServiceConfiguration configuration, MObject mObject) throws Exception {
        return dataService.insert(mObject, connectionService.getSql2Object(configuration),
                metadataManager.getMetadataTable(configuration, mObject.getDeveloperName()));
    }
}
