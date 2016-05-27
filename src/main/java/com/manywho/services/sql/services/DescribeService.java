package com.manywho.services.sql.services;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.api.draw.elements.type.TypeElementBinding;
import com.manywho.sdk.api.draw.elements.type.TypeElementProperty;
import com.manywho.sdk.api.draw.elements.type.TypeElementPropertyBinding;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;
import jersey.repackaged.com.google.common.collect.Lists;
import org.sql2o.data.Table;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DescribeService {

    @Inject
    public DescribeService(){}

    public TypeElement createTypeElementFromTableMetadata(TableMetadata tableMetadata) throws SQLException {
        HashMap<String, ContentType> metadataProperties = tableMetadata.getColumns();

        List<TypeElementProperty> properties = Lists.newArrayList();
        List<TypeElementPropertyBinding> propertyBindings = Lists.newArrayList();

        for(Map.Entry<String, ContentType> property: metadataProperties.entrySet()) {
            properties.add(new TypeElementProperty(property.getKey(), property.getValue()));
            propertyBindings.add(new TypeElementPropertyBinding(property.getKey(), property.getKey(), tableMetadata.getColumnsDatabaseType().get(property.getKey())));
        }

        List<TypeElementBinding> bindings = Lists.newArrayList();
        bindings.add(new TypeElementBinding(tableMetadata.getTableName(), "The binding for " + tableMetadata.getTableName(), tableMetadata.getTableName(), propertyBindings));

        return new TypeElement(tableMetadata.getTableName(), properties, bindings);
    }

    public List<Property> createProperties(Table table, TableMetadata tableMetadata) {
        HashMap<String, ContentType> columnsContentType = tableMetadata.getColumns();
        List<Property> properties = new ArrayList<>();

        properties.addAll(
                table.columns().stream()
                        .map(column -> new Property(column.getName(), "", columnsContentType.get(column.getName())))
                        .collect(Collectors.toList()));

        return properties;
    }

    public void populateProperty(String propertyName, String propertyValue, List<Property> propertyList) {
        for (Property p: propertyList) {
            if(Objects.equals(p.getDeveloperName(), propertyName)) {
                p.setContentValue(propertyValue);

                return;
            }
        }
    }
}
