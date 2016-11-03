package com.manywho.services.sql.suites.sqlserver.data;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.After;
import org.junit.Test;
import org.sql2o.Connection;

public class AutoIncrementTest extends ServiceFunctionalTest {

    @Test
    public void testCreateDataWithAutoIncrement() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("sqlserver");

        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                    "name character varying(255)," +
                    "id INT NOT NULL  IDENTITY (1,1) PRIMARY KEY," +
                    "description character varying(1024), " +
                    ");";
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion("/data",
                "suites/mysql/autoincrement/create-request.json",
                configurationParameters(),
                "suites/mysql/autoincrement/create-response.json",
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
