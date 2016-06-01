package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.describe.DescribeServiceResponse;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;

public class DefaultApiRequest {

    public static void describeServiceRequestAndAssertion(WebTarget webTarget, String requestPathFile, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {

        DescribeServiceResponse describeServiceResponse = webTarget.request()
                .post(Entity.entity(JsonFormatUtil.getObjectDataRequestFromFile(requestPathFile), MediaType.APPLICATION_JSON))
                .readEntity(DescribeServiceResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getDescribeServiceResponseAsJson(describeServiceResponse),
                false
        );
    }

    public static void loadDataRequestAndAssertion(WebTarget webTarget, String requestPathFile, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {

        ObjectDataResponse objectDataResponse = webTarget.request()
                .post(Entity.entity(JsonFormatUtil.getObjectDataRequestFromFile(requestPathFile), MediaType.APPLICATION_JSON))
                .readEntity(ObjectDataResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getObjectDataResponseAsJson(objectDataResponse),
                false
        );
    }

    public static void saveDataRequestAndAssertion(WebTarget webTarget, String requestPathFile, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {

        ObjectDataResponse objectDataResponse = webTarget.request()
                .put(Entity.entity(JsonFormatUtil.getObjectDataRequestFromFile(requestPathFile), MediaType.APPLICATION_JSON))
                .readEntity(ObjectDataResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getObjectDataResponseAsJson(objectDataResponse),
                false
        );
    }
}
