package com.manywho.services.sql.controllers.describe;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class DescribeTest extends BaseFunctionalTest {
    @Before
    public void setupDatabase() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/common/create-table-country.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testDescribe() throws Exception {
        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/without-types/metadata1-request.json",
                "controllers/describe/without-types/metadata1-response.json"
        );
    }

    @Test
    public void testDescribeWithTypes() throws Exception {
        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/with-types/metadata1-request.json",
                "controllers/describe/with-types/metadata1-response.json"
        );
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            connection.createQuery("DROP TABLE IF EXISTS public.country").executeUpdate();;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
