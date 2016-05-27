package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;
import com.manywho.services.sql.factories.MObjectFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.data.Table;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class DataService {
    private MObjectFactory mObjectFactory;
    private QueryService queryService;

    @Inject
    public DataService(MObjectFactory mObjectFactory, QueryService queryService) {
        this.mObjectFactory = mObjectFactory;
        this.queryService = queryService;
    }

    public List<MObject> getTableContentByPrimaryKey(TableMetadata tableMetadata, Sql2o sql2o, String id) throws SQLException {
        String search = tableMetadata.getPrimaryKeyName() + "= :idParam";
        String sql = "SELECT * FROM " + tableMetadata.getTableName() + " WHERE " + search;

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sql);
            String paramType = tableMetadata.getColumnsDatabaseType().get(tableMetadata.getPrimaryKeyName());
            queryService.populateParameter("idParam", id, paramType, query);
            Table table = query.executeAndFetchTable();

            return mObjectFactory.createFromTable(table, tableMetadata);
        } catch (DataBaseTypeNotSupported dataBaseTypeNotSupported) {
            dataBaseTypeNotSupported.printStackTrace();
        }

        return null;
    }

    public List<MObject> getTableContentBySearch(TableMetadata tableMetadata, Sql2o sql2o, String search) throws SQLException {
        String sql = "SELECT * FROM " + tableMetadata.getTableName() + " WHERE " + search;

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sql);
            Table table = query.executeAndFetchTable();

            return mObjectFactory.createFromTable(table, tableMetadata);
        }
    }

    public MObject update(MObject mObject, Sql2o sql2o, TableMetadata metadataTable) throws DataBaseTypeNotSupported {

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(queryService.createQueryWithParametersForUpdate("param", mObject, metadataTable));

            for(Property p : mObject.getProperties()) {
                queryService.populateParameter("param" + p.getDeveloperName(),
                        p.getContentValue(),
                        metadataTable.getColumnsDatabaseType().get(p.getDeveloperName()),
                        query);
            }

            query.executeUpdate();

            return mObject;
        }
    }
}
