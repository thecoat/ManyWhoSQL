package com.manywho.services.sql.suites.common.controllers.data;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

import static junit.framework.TestCase.assertEquals;

public class DeleteTest extends ServiceFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");

        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + scapeTableName("city") +
                    "(" +
                    "cityname character varying(255)," +
                    "countryname character varying(255)," +
                    "description character varying(1024)," +
                    "CONSTRAINT city_pk PRIMARY KEY (cityname, countryname)" +
                    ");";
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testDeleteDataByExternalId() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");
        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("city")+ "(cityname, countryname) VALUES ('Montevideo', 'Uruguay');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion("/data/delete",
                "suites/common/data/delete/request.json",
                configurationParameters(),
                "suites/common/data/delete/response.json",
                dispatcher
        );

        try (Connection connection = getSql2o().open()) {
            String sql = "SELECT count(cityname) From " + scapeTableName("city")+ " WHERE cityname = 'Montevideo' and countryname ='Uruguay';";
            int found = connection.createQuery(sql).executeScalar(Integer.class);
            assertEquals(0, found);
        }
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("city", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
