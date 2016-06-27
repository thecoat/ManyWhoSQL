package com.manywho.services.sql.suites.sqlserver.data;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;

import java.io.IOException;
import java.net.URISyntaxException;

public class DateTimeTest extends BaseFunctionalTest {
    @Before
    public void setupDatabase() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("sqlserver");
    }

    @Test
    public void testLoadDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            String sqlCreate = "CREATE TABLE " + scapeTableName("timetest") +
                    "(" +
                    "id integer NOT NULL," +
                    "datetime datetime," +
                    "datetime2 datetime2," +

                    "CONSTRAINT timetest_id_pk PRIMARY KEY (id)" +
                    ");";

            connection.createQuery(sqlCreate).executeUpdate();

            // todo not suported datetimeoffset
            String sql = "INSERT INTO " + scapeTableName("timetest") + "(id, datetime, datetime2) VALUES " +
                                                     "('1', '2007-05-08 12:35:29.123', '2007-05-08 12:35:29. 1234567');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),

                "suites/sqlserver/dates/load/request.json",
                configurationParameters(),
                "suites/sqlserver/dates/load/response.json"
        );
    }

    @Test
    public void testUpdateDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            String sqlCreate = "CREATE TABLE " + scapeTableName("timetest") +
                    "(" +
                    "id integer NOT NULL," +
                    "datetime datetime," +
                    "datetime2 datetime2," +
                    "datetimeoffset datetimeoffset," +
                    "CONSTRAINT timetest_id_pk PRIMARY KEY (id)" +
                    ");";

            connection.createQuery(sqlCreate).executeUpdate();

            String sql = "INSERT INTO " + scapeTableName("timetest") +"(id, datetime, datetime2, datetimeoffset) VALUES " +
                    "('1', '2007-05-08 12:35:29.123', '2007-05-08 12:35:29. 1234567', '2007-05-08 12:35:29.123+00:02');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "suites/sqlserver/dates/save/request-dates.json",
                configurationParameters(),
                "suites/sqlserver/dates/save/response-dates.json");
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("timetest", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
