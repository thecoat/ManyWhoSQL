package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
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
        List<MObject> mObjects;
        mObjects = dataService.getTableContentByPrimaryKey(
                        objectDataType,
                        metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName()),
                        connectionService.getConnection(configuration),
                        id
                    );

        return mObjects;
    }

    public ObjectDataResponse save(ServiceConfiguration serviceConfiguration, ObjectDataRequest objectDataRequest) throws Exception {
        throw new Exception("not working");

//        String objectDeveloperName = objectDataRequest.getObjectDataType().getDeveloperName();
//
//        List<MObject> mObjects = dataService.saveTableContent(
//                objectDataRequest,
//                metadataManager.getMetadataTable(serviceConfiguration,objectDeveloperName),
//                connectionService.getConnection(serviceConfiguration)
//        );
//
//        return new ObjectDataResponse(mObjects);
    }
}
