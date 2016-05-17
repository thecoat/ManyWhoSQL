package com.manywho.services.database.controllers;

import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.services.controllers.AbstractController;
import com.manywho.services.database.manager.DataManager;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class DataController extends AbstractController{

    private DataManager dataManager;

    @Inject
    public DataController(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Path("/data")
    @POST
    public ObjectDataResponse load(ObjectDataRequest objectDataRequest) {
        return dataManager.load(objectDataRequest);
    }
}
