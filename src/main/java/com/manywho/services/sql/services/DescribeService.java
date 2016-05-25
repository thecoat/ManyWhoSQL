package com.manywho.services.sql.services;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.services.sql.entities.TableMetadata;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DescribeService {

    @Inject
    public DescribeService(){}

    public TypeElement createTypeElementFromTableMetadata(TableMetadata tableMetadata) throws SQLException {
        TypeElement.SimpleTypeBuilder typeBuilder = new TypeElement.SimpleTypeBuilder()
                .setDeveloperName(tableMetadata.getTableName())
                .setTableName(tableMetadata.getTableName());

        HashMap<String, ContentType> properties = tableMetadata.getColumns();

        for(Map.Entry<String, ContentType> property: properties.entrySet()) {
            typeBuilder.addProperty(property.getKey(), property.getValue(), property.getKey());
        }

        return typeBuilder.build();
    }
}
