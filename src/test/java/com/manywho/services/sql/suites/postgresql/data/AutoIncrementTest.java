package com.manywho.services.sql.suites.postgresql.data;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.sql2o.Connection;

public class AutoIncrementTest extends ServiceFunctionalTest {

    @Test
    public void testCreateDataWithAutoIncrement() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");

        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                    "name character varying(255)," +
                    "description character varying(1024), " +
                    "id SERIAL NOT NULL," +
                    "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                    ");";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion("/data",
                "suites/postgresql/autoincrement/create-request.json",
                configurationParameters(),
                "suites/postgresql/autoincrement/create-response.json",
                dispatcher
        );
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("country", connection);
        } catch (ClassNotFoundException e) {
        }
    }
}
