package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.controllers.AbstractDataController;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.managers.DataManager;
import javax.ws.rs.*;
import javax.inject.Inject;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class DataController extends AbstractDataController{

    private DataManager dataManager;
    private ConfigurationParser configurationParser;

    @Inject
    public DataController(DataManager dataManager, ConfigurationParser configurationParser){
        this.dataManager = dataManager;
        this.configurationParser = configurationParser;
    }

    @Path("/data")
    @POST
    public ObjectDataResponse load(ObjectDataRequest objectDataRequest) throws Exception {
        ServiceConfiguration serviceConfiguration = configurationParser.from(objectDataRequest);

        return dataManager.load(serviceConfiguration, objectDataRequest);
    }

    @Path("/data")
    @PUT
    public ObjectDataResponse save(ObjectDataRequest objectDataRequest) throws Exception {
        ServiceConfiguration serviceConfiguration = configurationParser.from(objectDataRequest);

        return dataManager.save(serviceConfiguration, objectDataRequest);
    }

    @Override
    public ObjectDataResponse delete(ObjectDataRequest objectDataRequest) throws Exception {

        return null;
    }
}
