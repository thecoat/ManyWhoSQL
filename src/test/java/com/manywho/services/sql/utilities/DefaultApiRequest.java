package com.manywho.services.sql.utilities;

import com.google.common.io.Resources;
import com.manywho.sdk.api.describe.DescribeServiceResponse;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class DefaultApiRequest {

    public static void describeServiceRequestAndAssertion(WebTarget webTarget, String requestPathFile, HashMap<String, String> requestReplacements, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {

        DescribeServiceResponse describeServiceResponse = webTarget.request()
                .post(Entity.entity(getObjectDataRequestFromFile(requestPathFile,requestReplacements), MediaType.APPLICATION_JSON))
                .readEntity(DescribeServiceResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getDescribeServiceResponseAsJson(describeServiceResponse),
                false
        );
    }

    public static void loadDataRequestAndAssertion(WebTarget webTarget, String requestPathFile, HashMap<String, String> requestReplacements, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {
        ObjectDataResponse objectDataResponse = webTarget.request()
                .post(Entity.entity(getObjectDataRequestFromFile(requestPathFile,requestReplacements), MediaType.APPLICATION_JSON))
                .readEntity(ObjectDataResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getObjectDataResponseAsJson(objectDataResponse),
                false
        );
    }

    public static void saveDataRequestAndAssertion(WebTarget webTarget, String requestPathFile, HashMap<String, String> requestReplacements, String expectedResponsePathFile) throws IOException, URISyntaxException, JSONException {

        ObjectDataResponse objectDataResponse = webTarget.request()
                .put(Entity.entity(getObjectDataRequestFromFile(requestPathFile,requestReplacements), MediaType.APPLICATION_JSON))
                .readEntity(ObjectDataResponse.class);

        JSONAssert.assertEquals(
                JsonFormatUtil.getFileContentAsJson(expectedResponsePathFile),
                JsonFormatUtil.getObjectDataResponseAsJson(objectDataResponse),
                false
        );
    }

    private static ObjectDataRequest getObjectDataRequestFromFile(String requestPathFile, HashMap<String, String> requestReplacements) throws IOException, URISyntaxException {
        String fileContent = new String(Files.readAllBytes(Paths.get(Resources.getResource(requestPathFile).toURI())));

        for(String key: requestReplacements.keySet()) {
            fileContent = fileContent.replace(key, requestReplacements.get(key));
        }

        return JsonFormatUtil.getObjectDataRequestFromString(fileContent);
    }
}
