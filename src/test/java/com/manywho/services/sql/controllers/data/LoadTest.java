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
            String sql = Resources.toString(Resources.getResource("controllers/common/create-table-country.sql"), Charsets.UTF_8);
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testLoadDataByExternalId() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES ('1', 'Uruguay', 'It is a nice country');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/load/by-external-id/load-request.json",
                "controllers/data/load/by-external-id/load-response.json"
        );
    }

    @Test
    public void testLoadDataByEqualAndLikeFilter() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'It is a nice country')," +
                    "('2', 'England', 'It is a beautiful country');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/load/by-filter/equal-and-like/load-request.json",
                "controllers/data/load/by-filter/equal-and-like/load-response.json"
        );
    }

    @Test
    public void testLoadDataByEqualOrLikeFilter() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'It is a nice country')," +
                    "('2', 'England', 'It is a beautiful country');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/load/by-filter/equal-or-like/load-request.json",
                "controllers/data/load/by-filter/equal-or-like/load-response.json"
        );
    }

    @Test
    public void testLoadDataByFilterWithOffsetAndLimit() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO public.country(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'Uruguay description')," +
                    "('2', 'England', 'England description')," +
                    "('3', 'Spain', 'Spain description')," +
                    "('4', 'Italy', 'Italy description');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/load/by-filter/offset-and-limit/load-offset-request.json",
                "controllers/data/load/by-filter/offset-and-limit/load-offset-response.json"
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
