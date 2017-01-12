package com.manywho.services.sql.services;

import com.google.common.base.Strings;
import com.manywho.sdk.api.ContentType;
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

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            dataBaseTypeNotSupported.printStackTrace();
        }

        return null;
    }

    public List<MObject> fetchBySearch(TableMetadata tableMetadata, Sql2o sql2o, String sqlSearch) throws SQLException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sqlSearch).setCaseSensitive(true);

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
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

            query.setCaseSensitive(true).executeUpdate();

            return mObject;
        }
    }

    public MObject insert(MObject mObject, Sql2o sql2o, TableMetadata tableMetadata, ServiceConfiguration configuration) throws DataBaseTypeNotSupported, ParseException {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryStrService.createQueryWithParametersForInsert(mObject, tableMetadata, configuration), true);
            String autoIncrement = "";

            for(Property p : mObject.getProperties()) {
                if (!tableMetadata.isColumnAutoincrement(p.getDeveloperName())) {
                    try {
                        parameterSanitaizerService.addParameterValueToTheQuery(p.getDeveloperName(), p.getContentValue(),
                                tableMetadata.getColumnsDatabaseType().get(p.getDeveloperName()), query);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    autoIncrement = p.getDeveloperName();
                }
            }

            Object objects[] = query.executeUpdate().getKeys();

            if (!Strings.isNullOrEmpty(autoIncrement)) {

                List<Property> properties = mObject.getProperties();
                Property property = new Property();
                property.setDeveloperName(autoIncrement);
                property.setContentType(ContentType.Number);

                // for not postgres db
                if (objects.length<tableMetadata.getColumnNames().size()) {
                    property.setContentValue(String.valueOf(objects[0]));
                } else {
                    // for postgres db
                    property.setContentValue(String.valueOf(objects[tableMetadata.getColumnNames().indexOf(autoIncrement)]));
                }

                properties.add(property);

                mObject.setProperties(properties);
            }

            mObject.setExternalId(mobjectUtil.getPrimaryKeyValue(tableMetadata.getPrimaryKeyNames(), mObject.getProperties()));

            return mObject;
        }
    }

    public void deleteByPrimaryKey(TableMetadata tableMetadata, Sql2o sql2o, HashMap<String, String> externalId, ServiceConfiguration configuration) throws ParseException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryStrService.createQueryWithParametersForDeleteByPrimaryKey(tableMetadata, externalId.keySet(), configuration));

            for(String key: externalId.keySet()) {
                String paramType = tableMetadata.getColumnsDatabaseType().get(key);
                parameterSanitaizerService.addParameterValueToTheQuery(key, externalId.get(key), paramType, query);
            }

            query.executeUpdate();
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            throw new RuntimeException(dataBaseTypeNotSupported);
        }
    }
}
