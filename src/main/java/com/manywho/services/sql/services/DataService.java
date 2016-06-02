package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;
import com.manywho.services.sql.factories.MObjectFactory;
import com.manywho.services.sql.utilities.MobjectUtil;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class DataService {
    private MObjectFactory mObjectFactory;
    private QueryService queryService;
    private ParameterSanitaizerService parameterSanitaizerService;

    @Inject
    public DataService(MObjectFactory mObjectFactory, QueryService queryService, ParameterSanitaizerService parameterSanitaizerService) {
        this.mObjectFactory = mObjectFactory;
        this.queryService = queryService;
        this.parameterSanitaizerService = parameterSanitaizerService;
    }

    public List<MObject> fetchByPrimaryKey(TableMetadata tableMetadata, Sql2o sql2o, String externalId) throws SQLException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryService.createQueryWithParametersForSelectByPrimaryKey(tableMetadata, "idPrimaryKeyParam"));
            String paramType = tableMetadata.getColumnsDatabaseType().get(tableMetadata.getPrimaryKeyName());
            parameterSanitaizerService.populateParameter("idPrimaryKeyParam", externalId, paramType, query);

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            dataBaseTypeNotSupported.printStackTrace();
        }

        return null;
    }

    public List<MObject> fetchBySearch(TableMetadata tableMetadata, Sql2o sql2o, String sqlSearch) throws SQLException {
        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sqlSearch);

            return mObjectFactory.createFromTable(query.executeAndFetchTable(), tableMetadata);
        }
    }

    public MObject update(MObject mObject, Sql2o sql2o, TableMetadata metadataTable) throws DataBaseTypeNotSupported {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryService.createQueryWithParametersForUpdate("param", mObject, metadataTable));

            for(Property p : mObject.getProperties()) {
                parameterSanitaizerService.populateParameter("param" + p.getDeveloperName(),
                        p.getContentValue(),
                        metadataTable.getColumnsDatabaseType().get(p.getDeveloperName()),
                        query);
            }

            query.executeUpdate();

            return mObject;
        }
    }

    public MObject insert(MObject mObject, Sql2o sql2o, TableMetadata tableMetadata) throws DataBaseTypeNotSupported {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryService.createQueryWithParametersForInsert("param", mObject, tableMetadata ));

            for(Property p : mObject.getProperties()) {
                parameterSanitaizerService.populateParameter("param" + p.getDeveloperName(), p.getContentValue(),
                        tableMetadata.getColumnsDatabaseType().get(p.getDeveloperName()), query);
            }
            mObject.setExternalId(MobjectUtil.getPrimaryKeyValue(tableMetadata.getPrimaryKeyName(), mObject.getProperties()));
            query.executeUpdate();

            return mObject;
        }
    }
}
