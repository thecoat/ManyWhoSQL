package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.factories.MObjectFactory;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private MObjectFactory mObjectFactory;

    @Inject
    public DataService(MObjectFactory mObjectFactory) {
        this.mObjectFactory = mObjectFactory;
    }

    public List<MObject> getTableContentByPrimaryKey(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, Connection connection, String id) throws SQLException {
        String search = tableMetadata.getPrimaryKeyName() + '=' + id;

        return this.getTableContent(objectDataRequest, tableMetadata, connection, search);
    }

    public List<MObject> getTableContentBySearch(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, Connection connection, String search) throws SQLException {

        return this.getTableContent(objectDataRequest, tableMetadata, connection, search);
    }

    private List<MObject> getTableContent(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, Connection connection, String search) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rows = stmt.executeQuery("SELECT * FROM " + tableMetadata.getTableName() + " " + search);
        List<MObject> mObjectList = new ArrayList<>();

        while (rows.next()) {
            mObjectList.add(mObjectFactory.createFromTable(objectDataRequest, tableMetadata, rows));
        }

        return mObjectList;
    }

    public List<MObject> saveTableContent(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, Connection connection) throws Exception {

        throw new Exception("not implemented");
    }
}
