package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.DataService;
import com.manywho.services.sql.utilities.ObjectDataRequestUtil;

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

    public ObjectDataResponse load(ServiceConfiguration serviceConfiguration, ObjectDataRequest objectDataRequest) throws Exception {
        String objectDeveloperName = objectDataRequest.getObjectDataType().getDeveloperName();
        List<MObject> mObjects;

        if (ObjectDataRequestUtil.hasSearchFilter(objectDataRequest)) {
            mObjects = dataService.getTableContentBySearch(
                    objectDataRequest,
                    metadataManager.getMetadataTable(serviceConfiguration,objectDeveloperName),
                    connectionService.getConnection(serviceConfiguration),
                    objectDataRequest.getListFilter().getSearch()
            );

        } else if (ObjectDataRequestUtil.hasListFilterId(objectDataRequest)) {
            mObjects = dataService.getTableContentByPrimaryKey(
                        objectDataRequest,
                        metadataManager.getMetadataTable(serviceConfiguration,objectDeveloperName),
                        connectionService.getConnection(serviceConfiguration),
                        objectDataRequest.getListFilter().getId()
                    );
        } else {

            throw new Exception("");
        }

        return new ObjectDataResponse(mObjects);
    }

    public ObjectDataResponse save(ServiceConfiguration serviceConfiguration, ObjectDataRequest objectDataRequest) throws Exception {
        String objectDeveloperName = objectDataRequest.getObjectDataType().getDeveloperName();

        List<MObject> mObjects = dataService.saveTableContent(
                objectDataRequest,
                metadataManager.getMetadataTable(serviceConfiguration,objectDeveloperName),
                connectionService.getConnection(serviceConfiguration)
        );

        return new ObjectDataResponse(mObjects);
    }
}
