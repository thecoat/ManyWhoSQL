package com.manywho.services.sql.suites.common.controllers.data;

import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class LoadTest extends ServiceFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + scapeTableName("country") + "("+
                            "id integer NOT NULL,"+
                            "name character varying(255)," +
                            "description character varying(1024)," +
                            "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                        ");";

            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testLoadDataByExternalId() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO "+scapeTableName("country")+"(id, name, description) VALUES ('1', 'Uruguay', 'It is a nice country');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion("/data",
                "suites/common/data/load/by-external-id/load-request.json",
                configurationParameters(),
                "suites/common/data/load/by-external-id/load-response.json",
                dispatcher
        );
    }

    @Test
    public void testLoadDataByEqualAndLikeFilter() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO "+scapeTableName("country")+"(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'It is a nice country')," +
                    "('2', 'England', 'It is a beautiful country');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion("/data",
                "suites/common/data/load/by-filter/equal-and-like/load-request.json",
                configurationParameters(),
                "suites/common/data/load/by-filter/equal-and-like/load-response.json",
                dispatcher
        );
    }

    @Test
    public void testLoadDataByEqualOrLikeFilter() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("country") + "(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'It is a nice country')," +
                    "('2', 'England', 'It is a beautiful country');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion("/data",
                "suites/common/data/load/by-filter/equal-or-like/load-request.json",
                configurationParameters(),
                "suites/common/data/load/by-filter/equal-or-like/load-response.json",
                dispatcher
        );
    }

    @Test
    public void testLoadDataByFilterWithOffsetAndLimit() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("country") + "(id, name, description) VALUES " +
                    "('1', 'Uruguay', 'Uruguay description')," +
                    "('2', 'England', 'England description')," +
                    "('3', 'Spain', 'Spain description')," +
                    "('4', 'Italy', 'Italy description');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion("/data",
                "suites/common/data/load/by-filter/offset-and-limit/load-offset-request.json",
                configurationParameters(),
                "suites/common/data/load/by-filter/offset-and-limit/load-offset-response.json",
                dispatcher
        );
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("country", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
