package com.manywho.services.sql.suites.sqlserver.data;
import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sql2o.Connection;

import java.io.IOException;
import java.net.URISyntaxException;

public class DateTimeTest extends BaseFunctionalTest {
    @Before
    public void setupDatabase() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("sqlserver");

        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE " + scapeTableName("timetest") +
                            "(" +
                                "id integer NOT NULL," +
                                "datetime datetime," +
                                "datetime2 datetime2," +

                                "CONSTRAINT timetest_id_pk PRIMARY KEY (id)" +
                            ");";

            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void testLoadDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            // todo not suported datetimeoffset
            String sql = "INSERT INTO " + scapeTableName("timetest") + "(id, datetime, datetime2) VALUES " +
                                                     "('1', '2007-05-08 12:35:29.123', '2007-05-08 12:35:29. 1234567');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),

                "suites/sqlserver/dates/request.json",
                configurationParameters(),
                "suites/sqlserver/dates/response.json"
        );
    }

    @Test
    @Ignore
    public void testUpdateDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("timetest") +"(id, time_with_timezone, time_without_timezone, timestamp_with_timezone, timestamp_without_timezone) VALUES " +
                    "('1', '2012-05-24 14:09:08 +02:00', '2013-06-25 15:10:09 +02:00', '2014-07-26 14:00:00 +02:00', '2014-07-26 14:00:00');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "suites/postgresql/dates/save/request-dates.json",
                configurationParameters(),
                "suites/postgresql/dates/save/response-dates.json");
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
