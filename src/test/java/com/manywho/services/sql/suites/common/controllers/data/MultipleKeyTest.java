package com.manywho.services.sql.suites.common.controllers.data;

import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

public class MultipleKeyTest extends BaseFunctionalTest {

    @Before
    public void setupDatabase() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("sqlserver");

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
    public void testLoadDataByExternalId() throws Exception {

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("city")+ "(cityname, countryname) VALUES ('Montevideo', 'Uruguay');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "suites/common/data/multiple-primary-key/load/load-request.json",
                configurationParameters(),
                "suites/common/data/multiple-primary-key/load/load-response.json"
        );
    }

    @Test
    public void testCreate() throws Exception {

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "suites/common/data/multiple-primary-key/create/create-request.json",
                configurationParameters(),
                "suites/common/data/multiple-primary-key/create/create-response.json"
        );
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
