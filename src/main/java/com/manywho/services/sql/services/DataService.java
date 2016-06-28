package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;
import com.manywho.services.sql.factories.MObjectFactory;
import com.manywho.services.sql.utilities.MobjectUtil;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class DataService {
    private MObjectFactory mObjectFactory;
    private QueryStrService queryStrService;
    private QueryParameterService parameterSanitaizerService;
    private MobjectUtil mobjectUtil;

    @Inject
    public DataService(MObjectFactory mObjectFactory, QueryStrService queryStrService,
                       QueryParameterService parameterSanitaizerService, MobjectUtil mobjectUtil) {
        this.mObjectFactory = mObjectFactory;
        this.queryStrService = queryStrService;
        this.parameterSanitaizerService = parameterSanitaizerService;
        this.mobjectUtil = mobjectUtil;
    }

    public List<MObject> fetchByPrimaryKey(TableMetadata tableMetadata, Sql2o sql2o, HashMap<String, String> externalId, ServiceConfiguration configuration) throws SQLException, ParseException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryStrService.createQueryWithParametersForSelectByPrimaryKey(tableMetadata, externalId.keySet(), configuration));

            for(String key: externalId.keySet()) {
                String paramType = tableMetadata.getColumnsDatabaseType().get(key);
                parameterSanitaizerService.addParameterValueToTheQuery(key, externalId.get(key), paramType, query);
            }

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata, configuration);
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            dataBaseTypeNotSupported.printStackTrace();
        }

        return null;
    }

    public List<MObject> fetchBySearch(TableMetadata tableMetadata, Sql2o sql2o, String sqlSearch, ServiceConfiguration configuration) throws SQLException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sqlSearch);

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata, configuration);
        }
    }

    public MObject update(MObject mObject, Sql2o sql2o, TableMetadata metadataTable, HashMap<String, String> primaryKeyHashMap, ServiceConfiguration configuration) throws DataBaseTypeNotSupported, ParseException {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryStrService.createQueryWithParametersForUpdate(mObject, metadataTable, primaryKeyHashMap.keySet(), configuration));

            for(Property p : mObject.getProperties()) {
                parameterSanitaizerService.addParameterValueToTheQuery(p.getDeveloperName(),
                        p.getContentValue(),
                        metadataTable.getColumnsDatabaseType().get(p.getDeveloperName()),
                        query);
            }

            query.executeUpdate();

            return mObject;
        }
    }

    public MObject insert(MObject mObject, Sql2o sql2o, TableMetadata tableMetadata, ServiceConfiguration configuration) throws DataBaseTypeNotSupported, ParseException {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryStrService.createQueryWithParametersForInsert(mObject, tableMetadata, configuration));

            for(Property p : mObject.getProperties()) {
                parameterSanitaizerService.addParameterValueToTheQuery(p.getDeveloperName(), p.getContentValue(),
                        tableMetadata.getColumnsDatabaseType().get(p.getDeveloperName()), query);
            }
            mObject.setExternalId(mobjectUtil.getPrimaryKeyValue(tableMetadata.getPrimaryKeyNames(), mObject.getProperties()));
            query.executeUpdate();

            return mObject;
        }
    }
}
