package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.RawDatabase;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.exceptions.RecordNotFoundException;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.services.PrimaryKeyService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class Database implements RawDatabase<ServiceConfiguration> {
    private DataManager dataManager;
    private PrimaryKeyService primaryKeyService;

    @Inject
    public Database(DataManager dataManager, PrimaryKeyService primaryKeyService) {
        this.dataManager = dataManager;
        this.primaryKeyService = primaryKeyService;
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
            this.dataManager.update(configuration, object, primaryKeyService.deserializePrimaryKey(object.getExternalId()));
            List<MObject> mObjectList = this.dataManager.load(configuration, object.getDeveloperName(), primaryKeyService.deserializePrimaryKey(object.getExternalId()));

            if (mObjectList.size()>0) return mObjectList.get(0);

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