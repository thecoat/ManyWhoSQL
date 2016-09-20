package com.manywho.services.sql.types;

import com.manywho.sdk.api.describe.DescribeServiceRequest;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.managers.DescribeManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RawTypeProvider implements TypeProvider {

    private DescribeManager describeManager;
    private ConfigurationParser configurationParser;

    @Inject
    public RawTypeProvider(DescribeManager describeManager, ConfigurationParser configurationParser) {
        this.describeManager = describeManager;
        this.configurationParser = configurationParser;
    }

    @Override
    public boolean doesTypeExist(String s) {
        return true;
    }

    @Override
    public List<TypeElement> describeTypes(DescribeServiceRequest describeServiceRequest) {
        ServiceConfiguration serviceConfiguration = configurationParser.from(describeServiceRequest);

        try {
            if (describeServiceRequest.getConfigurationValues() != null) {
                return describeManager.getListTypeElementFromTableMetadata(serviceConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return new ArrayList<>();
    }
}
