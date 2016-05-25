package com.manywho.services.sql.factories;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MObjectFactory {

    @Inject
    public MObjectFactory(){}
    public MObject createFromTable(ObjectDataRequest objectDataRequest, TableMetadata tableMetadata, ResultSet resultSet) throws SQLException {
        List<Property> properties = new ArrayList<>();
        resultSet.getFetchSize();
        properties.add(new Property("ID", "1"));
        properties.add(new Property("NAME", "Uruguay"));
        return new MObject("country", "1", properties);
    }
}
