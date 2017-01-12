package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.DataService;
import com.manywho.services.sql.services.PrimaryKeyService;
import com.manywho.services.sql.services.QueryStrService;
import org.sql2o.Sql2o;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class DataManager {

    private ConnectionService connectionService;
    private DataService dataService;
    private MetadataManager metadataManager;
    private QueryStrService queryStrService;
    private PrimaryKeyService primaryKeyService;

    @Inject
    public DataManager(ConnectionService connectionService, DataService dataService, MetadataManager metadataManager,
                       QueryStrService queryStrService, PrimaryKeyService primaryKeyService){
        this.connectionService = connectionService;
        this.dataService = dataService;
        this.metadataManager = metadataManager;
        this.queryStrService = queryStrService;
        this.primaryKeyService = primaryKeyService;
    }

    public List<MObject> load(ServiceConfiguration configuration, TableMetadata tableMetadata,
                              HashMap<String, String> id) throws Exception {

        return dataService.fetchByPrimaryKey(tableMetadata, connectionService.getSql2Object(configuration), id, configuration);
    }

    public List<MObject> loadBySearch(ServiceConfiguration configuration, TableMetadata tableMetadata,
                                      ObjectDataType objectDataType, ListFilter filters) throws Exception {

        return dataService.fetchBySearch(tableMetadata, connectionService.getSql2Object(configuration),
                queryStrService.getSqlFromFilter(configuration, objectDataType, filters, tableMetadata));
    }

    public MObject update(ServiceConfiguration configuration, TableMetadata tableMetadata, MObject mObject) throws Exception {
        Sql2o sql2o = connectionService.getSql2Object(configuration);
        HashMap<String, String> primaryKeyHashMap = primaryKeyService.deserializePrimaryKey(mObject.getExternalId());
        dataService.update(mObject, sql2o, tableMetadata, primaryKeyHashMap, configuration);

        return dataService.fetchByPrimaryKey(tableMetadata, sql2o, primaryKeyHashMap, configuration).get(0);
    }

    public MObject create(ServiceConfiguration configuration, TableMetadata tableMetadata, MObject mObject) throws Exception {

        dataService.insert(mObject, connectionService.getSql2Object(configuration), tableMetadata, configuration);

        return mObject;
    }

    public void delete(ServiceConfiguration configuration,TableMetadata tableMetadata,
                       HashMap<String, String> id) throws Exception {

        dataService.deleteByPrimaryKey(tableMetadata, connectionService.getSql2Object(configuration),
                id, configuration);
    }
}
