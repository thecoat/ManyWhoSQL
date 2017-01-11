package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.sdk.services.database.RawDatabase;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.RecordNotFoundException;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.managers.MetadataManager;
import com.manywho.services.sql.services.PrimaryKeyService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class Database implements RawDatabase<ServiceConfiguration> {
    private DataManager dataManager;
    private PrimaryKeyService primaryKeyService;
    private MetadataManager metadataManager;

    @Inject
    public Database(DataManager dataManager, PrimaryKeyService primaryKeyService, MetadataManager metadataManager) {
        this.dataManager = dataManager;
        this.primaryKeyService = primaryKeyService;
        this.metadataManager = metadataManager;
    }

    @Override
    public MObject create(ServiceConfiguration configuration, MObject object) {
        try {
            dataManager.create(configuration, object);
            HashMap<String, String> primaryKey = primaryKeyService.deserializePrimaryKey(object.getExternalId());
            List<MObject> mObjectList = this.dataManager.load(configuration, object.getDeveloperName(), primaryKey);

            if(mObjectList.size()>0){
                return mObjectList.get(0);
            }

        } catch (Exception e) {
            throw new RuntimeException("problem creating object" + e.getMessage());
        }

        throw new RuntimeException("Error creating object");
    }

    @Override
    public List<MObject> create(ServiceConfiguration configuration, List<MObject> objects) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration configuration, MObject object) {
        try {
            this.dataManager.delete(configuration, object.getDeveloperName(), primaryKeyService.deserializePrimaryKey(object.getExternalId()));
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void delete(ServiceConfiguration configuration, List<MObject> objects) {
        //todo delete list of object;

        return;
    }

    @Override
    public MObject find(ServiceConfiguration configuration, ObjectDataType objectDataType, String id) {
        try {
            List<MObject> mObjectList = this.dataManager.load(configuration, objectDataType.getDeveloperName(), primaryKeyService.deserializePrimaryKey(id));

            if(mObjectList.size()>0) return mObjectList.get(0);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

        throw new RecordNotFoundException();
    }

    @Override
    public List<MObject> findAll(ServiceConfiguration configuration, ObjectDataType objectDataType, ListFilter filter) {
        try {
            return this.dataManager.loadBySearch(configuration, objectDataType,  filter);

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public MObject update(ServiceConfiguration configuration, MObject object) {
        try {
            TableMetadata tableMetadata = metadataManager.getMetadataTable(configuration, object.getDeveloperName());
            HashMap<String, String> idWithAlias = primaryKeyService.deserializePrimaryKey(object.getExternalId());
            HashMap<String, String> idProperties = new HashMap<>();
            idWithAlias.entrySet()
                    .forEach(property -> idProperties.put(tableMetadata.getColumnNameOrAlias(property.getKey()), property.getValue()));

            object.getProperties()
                    .forEach(p -> p.setDeveloperName( tableMetadata.getColumnNameOrAlias(p.getDeveloperName())));

            this.dataManager.update(configuration, tableMetadata, object, idProperties);

            List<MObject> mObjectList = this.dataManager.load(configuration, object.getDeveloperName(), idProperties);
            if (mObjectList.size()>0) {
                MObject mObject = mObjectList.get(0);
                mObject.getProperties().forEach(p -> p.setDeveloperName(tableMetadata.getColumnAliasOrName(p.getDeveloperName())));

                return mObject;
            }

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

        throw new RecordNotFoundException();
    }

    @Override
    public List<MObject> update(ServiceConfiguration configuration, List<MObject> objects) {
        return null;
    }
}