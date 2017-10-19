package com.manywho.services.sql.managers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.DataService;
import com.manywho.services.sql.services.PrimaryKeyService;
import com.manywho.services.sql.services.QueryStrService;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class DataManager {

    private DataService dataService;
    private QueryStrService queryStrService;
    private PrimaryKeyService primaryKeyService;

    @Inject
    public DataManager(DataService dataService, QueryStrService queryStrService, PrimaryKeyService primaryKeyService){
        this.dataService = dataService;
        this.queryStrService = queryStrService;
        this.primaryKeyService = primaryKeyService;
    }

    public List<MObject> load(Connection connection, ServiceConfiguration configuration, TableMetadata tableMetadata,
                              HashMap<String, String> id) throws Exception {

        return dataService.fetchByPrimaryKey(tableMetadata, connection, id, configuration);
    }

    public List<MObject> loadBySearch(Sql2o sql2o,ServiceConfiguration configuration, TableMetadata tableMetadata,
                                      ObjectDataType objectDataType, ListFilter filters) throws Exception {

        return dataService.fetchBySearch(tableMetadata, sql2o, queryStrService.getSqlFromFilter(configuration,
                objectDataType, filters, tableMetadata));
    }

    public MObject update(Connection connection, ServiceConfiguration configuration, TableMetadata tableMetadata, MObject mObject) throws Exception {

        HashMap<String, String> primaryKeyHashMap = primaryKeyService.deserializePrimaryKey(mObject.getExternalId());
        dataService.update(mObject, connection, tableMetadata, primaryKeyHashMap, configuration);

        return dataService.fetchByPrimaryKey(tableMetadata, connection, primaryKeyHashMap, configuration).get(0);
    }

    public MObject create(Connection connection, ServiceConfiguration configuration, TableMetadata tableMetadata, MObject mObject) throws Exception {

        dataService.insert(mObject, connection, tableMetadata, configuration);

        return mObject;
    }

    public void delete(Sql2o sql2o, ServiceConfiguration configuration, TableMetadata tableMetadata,
                       HashMap<String, String> id) throws Exception {

        dataService.deleteByPrimaryKey(tableMetadata, sql2o, id, configuration);
    }
}
