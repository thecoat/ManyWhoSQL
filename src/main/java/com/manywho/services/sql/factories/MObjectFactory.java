package com.manywho.services.sql.factories;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.DescribeService;
import com.manywho.services.sql.utilities.MobjectUtil;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MObjectFactory {
    private DescribeService describeService;

    @Inject
    public MObjectFactory(DescribeService describeService){
        this.describeService = describeService;
    }

    public List<MObject> createFromTable(Table table, TableMetadata tableMetadata) {
        List<MObject> mObjects = new ArrayList<>();

        for (Row row: table.rows()) {
            List<Property> properties = describeService.createProperties(table, tableMetadata);

            for (Property property : properties) {
                describeService.populateProperty(property.getDeveloperName(), row.getString(property.getDeveloperName()), properties);
            }

            mObjects.add(new MObject(tableMetadata.getTableName(), MobjectUtil.getPrimaryKeyValue(tableMetadata.getPrimaryKeyName(), properties), properties));
        }

        return mObjects;
    }
}
