package com.manywho.services.sql.controllers.data;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class MultipleKeyTest extends BaseFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/data/multiple-primary-key/create-table-multiple-primary-key.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testLoadDataByExternalId() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.city(cityname, countryname) VALUES ('Montevideo', 'Uruguay');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/multiple-primary-key/load/load-request.json",
                "controllers/data/multiple-primary-key/load/load-response.json"
        );
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            connection.createQuery("DROP TABLE IF EXISTS public.city").executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
