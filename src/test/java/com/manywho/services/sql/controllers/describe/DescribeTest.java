package com.manywho.services.sql.controllers.describe;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import org.sql2o.Connection;

import java.io.IOException;
import java.net.URISyntaxException;

public class DescribeTest extends BaseFunctionalTest {

    @Test
    public void testDescribe() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/common/create-table-country.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/without-types/metadata1-request.json",
                getDefaultRequestReplacements(),
                "controllers/describe/without-types/metadata1-response.json");
    }

    @Test
    public void testDescribeWithTypes() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/common/create-table-country.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/with-types/metadata1-request.json",
                getDefaultRequestReplacements(),
                "controllers/describe/with-types/metadata1-response.json");
    }

    @Test
    public void testIgnoringTimeBecauseIsNotSupportedType() throws JSONException, IOException, URISyntaxException, ClassNotFoundException {
            try (Connection connection = getSql2o().open()) {
                String sql2 = Resources.toString(Resources.getResource("controllers/describe/not-supported-types/create-dates-table.sql"), Charsets.UTF_8);
                connection.createQuery(sql2).executeUpdate();

                String sql1 = "INSERT INTO servicesql.timetest(id, time, description) VALUES " +
                        "('1', '14:09:08', 'time description');";

                connection.createQuery(sql1).executeUpdate();
            }

            DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                    "controllers/describe/not-supported-types/metadata1-request.json",
                    getDefaultRequestReplacements(),
                    "controllers/describe/not-supported-types/metadata1-response.json");
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("servicesql.country", connection);
            deleteTableIfExist("servicesql.timetest", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
