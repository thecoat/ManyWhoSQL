package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.RawDatabase;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.exceptions.RecordNotFoundException;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.managers.MetadataManager;
import com.manywho.services.sql.services.AliasService;
import com.manywho.services.sql.services.PrimaryKeyService;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class Database implements RawDatabase<ServiceConfiguration> {
    private DataManager dataManager;
    private PrimaryKeyService primaryKeyService;
    private MetadataManager metadataManager;
    private AliasService aliasService;

    @Inject
    public Database(DataManager dataManager, PrimaryKeyService primaryKeyService, MetadataManager metadataManager,
                    AliasService aliasService) {
        this.dataManager = dataManager;
        this.primaryKeyService = primaryKeyService;
        this.metadataManager = metadataManager;
        this.aliasService = aliasService;
    }

    @Override
    public MObject create(ServiceConfiguration configuration, MObject object) {
        try {
            TableMetadata tableMetadata = metadataManager.getMetadataTable(configuration, object.getDeveloperName());
            MObject objectWithColumnName = this.aliasService.getMObjectWithoutAliases(object, tableMetadata);
            MObject objectInserted = dataManager.create(configuration, tableMetadata, objectWithColumnName);
            HashMap<String, String> primaryKey = primaryKeyService.deserializePrimaryKey(objectInserted.getExternalId());
            List<MObject> mObjectList = this.dataManager.load(configuration, tableMetadata, primaryKey);

            if(mObjectList.size()>0){
                return this.aliasService.getMObjectWithAliases(mObjectList.get(0), tableMetadata);
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
            TableMetadata tableMetadata = metadataManager.getMetadataTable(configuration, object.getDeveloperName());
            MObject objectWithoutAliases = this.aliasService.getMObjectWithoutAliases(object, tableMetadata);

            this.dataManager.delete(configuration,tableMetadata,
                    primaryKeyService.deserializePrimaryKey(objectWithoutAliases.getExternalId()));

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
            TableMetadata tableMetadata = metadataManager.getMetadataTable(configuration, objectDataType.getDeveloperName());

            List<MObject> mObjectList = this.dataManager.load(configuration, tableMetadata,
                    primaryKeyService.deserializePrimaryKey(id));

            if(mObjectList.size()>0) return this.aliasService.getMObjectWithAliases(mObjectList.get(0), tableMetadata);
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
            object = aliasService.getMObjectWithoutAliases(object, tableMetadata);

            this.dataManager.update(configuration, tableMetadata, object);

            List<MObject> mObjectList = this.dataManager.load(configuration, tableMetadata,
                    primaryKeyService.deserializePrimaryKey(object.getExternalId()));

            if (mObjectList.size()>0) {
                MObject mObject = mObjectList.get(0);
                return this.aliasService.getMObjectWithAliases(mObject, tableMetadata);
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