package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.services.sql.entities.TableMetadata;
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

    @Inject
    public DataService(MObjectFactory mObjectFactory) {
        this.mObjectFactory = mObjectFactory;
    }

    public List<MObject> getTableContentByPrimaryKey(ObjectDataType objectDataType, TableMetadata tableMetadata, Sql2o sql2o, String id) throws SQLException {
        String search = tableMetadata.getPrimaryKeyName() + "= :idParam";
        String sql = "SELECT * FROM " + tableMetadata.getTableName() + " WHERE " + search;

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sql);

            if(tableMetadata.needToCastPrimaryKeyToInteger()) {
                query.addParameter("idParam", Integer.parseInt(id));
            }

            Table table = query.executeAndFetchTable();

            return mObjectFactory.createFromTable(table, tableMetadata);
        }
    }

    public List<MObject> getTableContentBySearch(ObjectDataType objectDataRequest, TableMetadata tableMetadata, Sql2o connection, String search) throws SQLException {

        return this.getTableContent(objectDataRequest, tableMetadata, connection, search);
    }

    private List<MObject> getTableContent(ObjectDataType objectDataType, TableMetadata tableMetadata, Sql2o sql2o, String search) throws SQLException {
        String sql = "SELECT * FROM " + tableMetadata.getTableName() + " WHERE " + search;

        try(Connection con = sql2o.open()) {
            Query query = con.createQuery(sql);
            Table table = query.executeAndFetchTable();

            return mObjectFactory.createFromTable(table, tableMetadata);
        }
    }

    public List<MObject> saveTableContent(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, Sql2o connection) throws Exception {

        throw new Exception("not implemented");
    }
}
