package com.manywho.services.sql.factories;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.DescribeService;
import com.manywho.services.sql.services.PrimaryKeyService;
import com.manywho.services.sql.utilities.MobjectUtil;
import org.sql2o.data.Row;
import org.sql2o.data.Table;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MObjectFactory {
    private DescribeService describeService;
    private MobjectUtil mobjectUtil;
    private PrimaryKeyService primaryKeyService;

    @Inject
    public MObjectFactory(DescribeService describeService, MobjectUtil mobjectUtil, PrimaryKeyService primaryKeyService){
        this.describeService = describeService;
        this.mobjectUtil = mobjectUtil;
        this.primaryKeyService = primaryKeyService;
    }

    public List<MObject> createFromTable(Table table, TableMetadata tableMetadata, ServiceConfiguration configuration) {
        List<MObject> mObjects = new ArrayList<>();

        for (Row row: table.rows()) {
            List<Property> properties = describeService.createProperties(table, tableMetadata);

            for (Property property : properties) {
                if (property.getContentType() == com.manywho.sdk.api.ContentType.DateTime) {
                    describeService.populatePropertyDate(property.getDeveloperName(), row.getObject(property.getDeveloperName()), properties);
                } else {
                    describeService.populateProperty(property.getDeveloperName(), row.getString(property.getDeveloperName()), properties);
                }
            }

            HashMap<String, String> primaryKeyAlias = new HashMap<>();
            mobjectUtil.getPrimaryKeyProperties(tableMetadata.getPrimaryKeyNames(), properties)
                    .entrySet()
                    .forEach(p -> primaryKeyAlias.put(tableMetadata.getColumnAliasOrName(p.getKey()), p.getValue()));

            mObjects.add(new MObject(tableMetadata.getTableName(), primaryKeyService.serializePrimaryKey(primaryKeyAlias), properties));
            renamePropertiesUsingAliases(tableMetadata, properties);
        }

        return mObjects;
    }

    private void renamePropertiesUsingAliases(TableMetadata tableMetadata, List<Property> originalProperties) {
        originalProperties.forEach(p -> p.setDeveloperName(tableMetadata.getColumnAliasOrName(p.getDeveloperName())));
    }
}
