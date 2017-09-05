package com.manywho.services.sql.types;

import com.google.common.base.Strings;
import com.manywho.sdk.api.describe.DescribeServiceRequest;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.managers.DescribeManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RawTypeProvider implements TypeProvider<ServiceConfiguration> {

    private DescribeManager describeManager;

    @Inject
    public RawTypeProvider(DescribeManager describeManager) {
        this.describeManager = describeManager;
    }

    @Override
    public boolean doesTypeExist(ServiceConfiguration configuration, String s) {
        return true;
    }

    @Override
    public List<TypeElement> describeTypes(ServiceConfiguration configuration, DescribeServiceRequest describeServiceRequest) {
        try {
            if (describeServiceRequest.getConfigurationValues() != null && describeServiceRequest.getConfigurationValues().size()>0) {
                if (!configuration.getNoUseSsl() && Strings.isNullOrEmpty(configuration.getServerPublicCertificate())) {
                    throw new RuntimeException("The Server Public Certificate is mandatory if you use SSL");
                }

                return describeManager.getListTypeElementFromTableMetadata(configuration);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>();
    }
}
