package com.manywho.services.sql.controllers.describe;

import com.manywho.services.sql.BaseFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import org.sql2o.Connection;

import java.io.IOException;
import java.net.URISyntaxException;

public class DescribeTest extends BaseFunctionalTest {

    @Test
    public void testDescribe() throws Exception {
        String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                        "id integer NOT NULL," +
                        "name character varying(255)," +
                        "description character varying(1024)," +
                        "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                    ");";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/without-types/metadata1-request.json",
                getDefaultRequestReplacements(),
                "controllers/describe/without-types/metadata1-response.json");
    }

    @Test
    public void testDescribeWithTypes() throws Exception {
        String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                "id integer NOT NULL," +
                "name character varying(255)," +
                "description character varying(1024)," +
                "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                ");";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/with-types/metadata1-request.json",
                getDefaultRequestReplacements(),
                "controllers/describe/with-types/metadata1-response.json");
    }

    @Test
    public void testIgnoringTimeBecauseIsNotSupportedType() throws JSONException, IOException, URISyntaxException, ClassNotFoundException {
        String sql = "CREATE TABLE " + scapeTableName("timetest") + "(" +
                "id integer NOT NULL," +
                "time time," +
                "description character varying(255)," +
                "CONSTRAINT timetest_id_pk PRIMARY KEY (id)" +
                ");";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();

            String sql1 = "INSERT INTO "+scapeTableName("timetest")+"(id, time, description) VALUES " +
                    "('1', '14:09:08', 'time description');";

            connection.createQuery(sql1).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion(target("/metadata"),
                "controllers/describe/not-supported-types/metadata1-request.json",
                getDefaultRequestReplacements(),
                "controllers/describe/not-supported-types/metadata1-response.json");
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("country", connection);
            deleteTableIfExist("timetest", connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
