package com.manywho.services.sql.factories;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.DescribeService;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import javax.inject.Inject;
import java.util.*;

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

            for (Property p : properties) {
                describeService.populateProperty(p.getDeveloperName(), row.getString(p.getDeveloperName()), properties);
            }

            mObjects.add(new MObject(tableMetadata.getTableName(), getPrimaryKeyValue(tableMetadata.getPrimaryKeyName(), properties), properties));
        }

        return mObjects;
    }

    public String getPrimaryKeyValue(String primaryKeyName, List<Property> properties) {
        for (Property p:properties) {
            if(Objects.equals(p.getDeveloperName(), primaryKeyName)) {
                return p.getContentValue();
            }
        }

        return "";
    }
}
