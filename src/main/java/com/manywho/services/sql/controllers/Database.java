package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.RawDatabase;
import com.manywho.sdk.services.database.RecordNotFoundException;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.managers.DataManager;

import javax.inject.Inject;
import java.util.List;

public class Database implements RawDatabase<ServiceConfiguration, MObject> {
    private DataManager dataManager;

    @Inject
    public Database(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public MObject create(ServiceConfiguration configuration, MObject object) {
        try {
            return dataManager.create(configuration, object);
        } catch (Exception e) {
            throw  new RuntimeException("problem creating object");
        }
    }

    @Override
    public List<MObject> create(ServiceConfiguration configuration, List<MObject> objects) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration configuration, MObject object) {

    }

    @Override
    public void delete(ServiceConfiguration configuration, List<MObject> objects) {

    }

    @Override
    public MObject find(ServiceConfiguration configuration, ObjectDataType objectDataType, String id) {
        try {
            List<MObject> mObjectList = this.dataManager.load(configuration, objectDataType, id);

            if(mObjectList.size()>0) return mObjectList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RecordNotFoundException();
    }

    @Override
    public List<MObject> findAll(ServiceConfiguration configuration, ObjectDataType objectDataType, ListFilter filter) {
        try {
            return this.dataManager.loadBySearch(configuration, objectDataType,  filter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RecordNotFoundException();
    }

    @Override
    public MObject update(ServiceConfiguration configuration, MObject object) {
        try {
            return this.dataManager.update(configuration, object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Error updating row for " + object.getDeveloperName());
    }

    @Override
    public List<MObject> update(ServiceConfiguration configuration, List<MObject> objects) {
        return null;
    }
}