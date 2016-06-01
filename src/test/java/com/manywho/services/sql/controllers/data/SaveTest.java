package com.manywho.services.sql.controllers.data;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class SaveTest extends BaseFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = Resources.toString(Resources.getResource("controllers/common/createTableCountry.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testCreateData() throws Exception {

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "controllers/data/save/create/create-request.json",
                "controllers/data/save/create/create-response.json"
        );
    }

    @Test
    public void testUpdateData() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES ('1', 'Uruguay', 'It is a nice country');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "controllers/data/save/update/update-request.json",
                "controllers/data/save/update/update-response.json"
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
