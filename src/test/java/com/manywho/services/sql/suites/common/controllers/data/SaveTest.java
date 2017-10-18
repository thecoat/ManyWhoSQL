package com.manywho.services.sql.suites.common.controllers.data;

import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Test;
import org.sql2o.Connection;

public class SaveTest extends ServiceFunctionalTest {
    @Test
    public void testCreate() throws Exception {
        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + escapeTableName("country") + "(" +
                    "id integer NOT NULL," +
                    "name character varying(255)," +
                    "description character varying(1024), " +
                    "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                    ");";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion("/data",
                "suites/common/data/save/create/create-request.json",
                configurationParameters(),
                "suites/common/data/save/create/create-response.json",
                dispatcher
        );
    }

    @Test
    public void testUpdate() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");
        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + escapeTableName("country") + "(" +
                    "id integer NOT NULL," +
                    "name character varying(255)," +
                    "description character varying(1024), " +
                    "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                    ");";
            connection.createQuery(sql).executeUpdate();
        }

        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + escapeTableName("country") + "(id, name, description) VALUES ('1', 'Uruguay', 'It is a nice country');";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion("/data",
                "suites/common/data/save/update/update-request.json",
                configurationParameters(),
                "suites/common/data/save/update/update-response.json",
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
