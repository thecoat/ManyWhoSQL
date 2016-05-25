package com.manywho.services.sql.controllers;

import com.manywho.sdk.api.describe.DescribeServiceRequest;
import com.manywho.sdk.api.describe.DescribeServiceResponse;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.controllers.DefaultDescribeController;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.managers.DescribeManager;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/metadata1")
@Consumes({"application/json"})
@Produces({"application/json"})
public class DescribeController{

    private DefaultDescribeController defaultDescribeController;
    private DescribeManager describeManager;
    private ConfigurationParser configurationParser;

    @Inject
    public DescribeController(DefaultDescribeController defaultDescribeController, DescribeManager describeManager, ConfigurationParser configurationParser) {
        this.defaultDescribeController = defaultDescribeController;
        this.describeManager = describeManager;
        this.configurationParser = configurationParser;
    }

    @POST
    public DescribeServiceResponse describe(@Valid @NotNull DescribeServiceRequest request) throws Exception {
        ServiceConfiguration serviceConfiguration = configurationParser.from(request);

        DescribeServiceResponse describeServiceResponse =  defaultDescribeController.describe(request);

        List<TypeElement> listTypeElements = describeServiceResponse.getInstall().getTypeElements();
        listTypeElements.addAll(describeManager.getListTypeElementFromTableMetadata(serviceConfiguration));

        return describeServiceResponse;
    }
}
