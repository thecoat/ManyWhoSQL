package com.manywho.services.sql.controllers.data;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class LoadTest extends BaseFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/common/createTableCountry.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testLoadData() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES ('1', 'Uruguay', 'It is a nice country');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/load/data-load1-request.json",
                "controllers/data/load/data-load1-response.json"
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