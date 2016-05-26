package com.manywho.services.sql.factories;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class MObjectFactory {

    @Inject
    public MObjectFactory(){}

    public MObject createFromTable(Table table, TableMetadata tableMetadata) {
        List<Property> properties = new ArrayList<>();
        HashMap<String, ContentType> columnsContentType = tableMetadata.getColumns();
        String id = "";

        properties.addAll(
                table.columns().stream()
                        .map(column -> new Property(column.getName(), "", columnsContentType.get(column.getName())))
                        .collect(Collectors.toList()));

        for(Row row: table.rows()) {
            for(Property property: properties) {
                Map<String, Object> objectMap = row.asMap();
                property.setContentValue(objectMap.get(property.getDeveloperName()).toString());

                if(Objects.equals(property.getDeveloperName(), tableMetadata.getPrimaryKeyName())) {
                    id = objectMap.get(property.getDeveloperName()).toString();
                }
            }
        }

        return new MObject(table.getName(), id , properties);
    }
}
