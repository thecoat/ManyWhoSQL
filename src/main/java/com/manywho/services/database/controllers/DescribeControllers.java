package com.manywho.services.database.controllers;

import com.manywho.sdk.entities.describe.DescribeServiceResponse;
import com.manywho.sdk.entities.describe.DescribeValue;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.translate.Culture;
import com.manywho.sdk.entities.draw.elements.type.TypeElement;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.services.controllers.AbstractController;
import com.manywho.sdk.services.describe.DescribeServiceBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class DescribeControllers extends AbstractController {
    @Path("/metadata")
    @POST
    public DescribeServiceResponse describe(ServiceRequest serviceRequest) throws Exception {
        return new DescribeServiceBuilder()
                .setProvidesDatabase(true)
                .setProvidesAutoBinding(true)
                .setCulture(new Culture("EN", "US"))
                .addConfigurationValue(new DescribeValue("Host", ContentType.String, true))
                .addConfigurationValue(new DescribeValue("Username", ContentType.String, true))
                .addConfigurationValue(new DescribeValue("Password", ContentType.Password, true))
                .addConfigurationValue(new DescribeValue("Database Type", ContentType.String, true))
                .createDescribeService()
                .createResponse();
    }

    @Path("/metadata/binding")
    @POST
    public TypeElement describeBinding(TypeElement typeElement) throws Exception {
        throw new Exception();
        // return describeManager.describeBinding(getAuthenticatedWho(), typeElement);
    }
}
