package com.manywho.services.sql.controllers.data;
import com.manywho.services.sql.BaseFunctionalTest;
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
        try (Connection connection = getSql2o().open()) {
            String sql = "CREATE TABLE "+scapeTableName("timetest") +
                            "(" +
                                "id integer NOT NULL," +
                                "time_with_timezone time," +
                                "time_without_timezone time," +
                                "timestamp_with_timezone timestamp," +
                                "CONSTRAINT timetest_id_pk PRIMARY KEY (id)" +
                            ");";
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    @Ignore("sqlserver doesn't allow insert specific timestamp")
    public void testLoadDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("timetest") + "(id, time_with_timezone, time_without_timezone, timestamp_with_timezone, timestamp_without_timezone) VALUES " +
                                                     "('1', '2012-05-24 14:09:08 +02:00', '2013-06-25 15:10:09 +02:00', '2014-07-26 14:00:00 +02:00', '2014-07-26 14:00:00');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.loadDataRequestAndAssertion(target("/data"),
                "controllers/data/dates/load/request-dates.json",
                getDefaultRequestReplacements(),
                "controllers/data/dates/load/response-dates.json"
        );
    }

    @Test
    @Ignore("sqlserver doesn't allow insert specific timestamp")
    public void testUpdateDatesAndTimes() throws ClassNotFoundException, JSONException, IOException, URISyntaxException {
        try (Connection connection = getSql2o().open()) {
            String sql = "INSERT INTO " + scapeTableName("timetest") +"(id, time_with_timezone, time_without_timezone, timestamp_with_timezone, timestamp_without_timezone) VALUES " +
                    "('1', '2012-05-24 14:09:08 +02:00', '2013-06-25 15:10:09 +02:00', '2014-07-26 14:00:00 +02:00', '2014-07-26 14:00:00');";

            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.saveDataRequestAndAssertion(target("/data"),
                "controllers/data/dates/save/request-dates.json",
                getDefaultRequestReplacements(),
                "controllers/data/dates/save/response-dates.json");
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
