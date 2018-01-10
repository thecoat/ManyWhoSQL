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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataService {
    private MObjectFactory mObjectFactory;
    private QueryStrService queryStrService;
    private QueryParameterService parameterSanitaizerService;
    private MobjectUtil mobjectUtil;
    private static final Logger LOGGER = LogManager.getLogger("com.manywho.services.sql");

    @Inject
    public DataService(MObjectFactory mObjectFactory, QueryStrService queryStrService,
                       QueryParameterService parameterSanitaizerService, MobjectUtil mobjectUtil) {
        this.mObjectFactory = mObjectFactory;
        this.queryStrService = queryStrService;
        this.parameterSanitaizerService = parameterSanitaizerService;
        this.mobjectUtil = mobjectUtil;
    }

    public List<MObject> fetchByPrimaryKey(TableMetadata tableMetadata, Connection connection, HashMap<String, String> externalId, ServiceConfiguration configuration) throws SQLException, ParseException {
        String queryString = "";
        try {
            queryString = queryStrService.createQueryWithParametersForSelectByPrimaryKey(tableMetadata, externalId.keySet(), configuration);
            Query query = connection.createQuery(queryString);

            for (String key : externalId.keySet()) {
                String paramType = tableMetadata.getColumnsDatabaseType().get(key);
                parameterSanitaizerService.addParameterValueToTheQuery(key, externalId.get(key), paramType, query);
            }

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
        } catch(DataBaseTypeNotSupported ex) {
            LOGGER.debug("query: " + queryString);
            throw new RuntimeException(ex.getMessage());
        } catch (RuntimeException ex) {
            LOGGER.debug("query: " + queryString);
            throw ex;
        }
    }

    public List<MObject> fetchBySearch(TableMetadata tableMetadata, Sql2o sql2o, String sqlSearch) throws SQLException {
        try (Connection con = sql2o.open()) {
            Query query = con.createQuery(sqlSearch).setCaseSensitive(true);

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
        } catch (RuntimeException ex) {
            LOGGER.debug("query: " + sqlSearch);
            throw ex;
        }
    }

    public MObject update(MObject mObject, Connection connection, TableMetadata metadataTable, HashMap<String, String> primaryKeyHashMap, ServiceConfiguration configuration) throws DataBaseTypeNotSupported, ParseException {
        String queryString = queryStrService.createQueryWithParametersForUpdate(mObject, metadataTable, primaryKeyHashMap.keySet(), configuration);

        Query query = connection.createQuery(queryString);

        for (Property p : mObject.getProperties()) {
            parameterSanitaizerService.addParameterValueToTheQuery(p.getDeveloperName(),
                    p.getContentValue(),
                    metadataTable.getColumnsDatabaseType().get(p.getDeveloperName()),
                    query);
        }

        try {
            query.setCaseSensitive(true).executeUpdate();
        }catch (RuntimeException ex) {
            LOGGER.debug("query: " + queryString);
            throw ex;
        }

        return mObject;
    }

    public MObject insert(MObject mObject, Connection connection, TableMetadata tableMetadata, ServiceConfiguration configuration) throws DataBaseTypeNotSupported, ParseException {
        String queryString = "";
        try {
            queryString = queryStrService.createQueryWithParametersForInsert(mObject, tableMetadata, configuration);
            Query query = connection.createQuery(queryString, true);
            String autoIncrement = "";

            for (Property p : mObject.getProperties()) {
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
                if (objects.length < tableMetadata.getColumnNames().size()) {
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
        } catch (RuntimeException ex) {
            LOGGER.debug("query: " + queryString);
            throw ex;
        }
    }

    public void deleteByPrimaryKey(TableMetadata tableMetadata, Sql2o sql2o, HashMap<String, String> externalId, ServiceConfiguration configuration) throws ParseException {
        String queryString = "";

        try (Connection con = sql2o.open()) {
            queryString = queryStrService.createQueryWithParametersForDeleteByPrimaryKey(tableMetadata, externalId.keySet(), configuration);
            Query query = con.createQuery(queryString);

            for (String key : externalId.keySet()) {
                String paramType = tableMetadata.getColumnsDatabaseType().get(key);
                parameterSanitaizerService.addParameterValueToTheQuery(key, externalId.get(key), paramType, query);
            }

            query.executeUpdate();
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            throw new RuntimeException(dataBaseTypeNotSupported);
        } catch (RuntimeException ex) {
            LOGGER.debug("query: " + queryString);
        }
    }
}
