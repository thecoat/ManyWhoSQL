package com.manywho.services.sql.suites.common.controllers.describe;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import org.sql2o.Connection;

import java.io.IOException;
import java.net.URISyntaxException;

public class DescribeTest extends ServiceFunctionalTest {

    @Test
    public void testDescribe() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");
        String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                        "id integer NOT NULL," +
                        "name character varying(255)," +
                        "description character varying(1024)," +
                        "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                    ");";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion("/metadata",
                "suites/common/describe/without-types/metadata1-request.json",
                configurationParameters(),
                "suites/common/describe/without-types/metadata1-response.json",
                dispatcher
        );
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

        DefaultApiRequest.describeServiceRequestAndAssertion("/metadata",
                "suites/common/describe/with-types/metadata1-request.json",
                configurationParameters(),
                "suites/common/describe/with-types/metadata1-response.json",
                dispatcher
        );
    }

    @Test
    public void testDescribeWithAliases() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");

        String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                "id integer NOT NULL," +
                "name character varying(255)," +
                "description character varying(1024)," +
                "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                ");";

        String aliasName = "COMMENT ON COLUMN " + scapeTableName("country") + ".name IS '{{ManyWhoName:The Name}}';";
        //ignored at the moment
        String aliasTable = "COMMENT ON TABLE " + scapeTableName("country") + " IS '{{ManyWhoName:The Country}}';";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();
            connection.createQuery(aliasName).executeUpdate();
            connection.createQuery(aliasTable).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion("/metadata",
                "suites/common/describe/with-alias-types/metadata1-request.json",
                configurationParameters(),
                "suites/common/describe/with-alias-types/metadata1-response.json",
                dispatcher
        );
    }

    @Test(expected = Exception.class)
    public void testDescribeWithTypesButNotPort() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");
        String sql = "CREATE TABLE " + scapeTableName("country") + "(" +
                "id integer NOT NULL," +
                "name character varying(255)," +
                "description character varying(1024)," +
                "CONSTRAINT country_id_pk PRIMARY KEY (id)" +
                ");";

        try (Connection connection = getSql2o().open()) {
            connection.createQuery(sql).executeUpdate();
        }

        DefaultApiRequest.describeServiceRequestAndAssertion("/metadata",
                "suites/common/describe/with-types-no-password/metadata1-request.json",
                configurationParameters(),
                "suites/common/describe/with-types-no-password/metadata1-response.json",
                dispatcher
        );
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

        DefaultApiRequest.describeServiceRequestAndAssertion("/metadata",
                "suites/common/describe/not-supported-types/metadata1-request.json",
                configurationParameters(),
                "suites/common/describe/not-supported-types/metadata1-response.json",
                dispatcher
        );
    }

    @After
    public void cleanDatabaseAfterEachTest() {
        try (Connection connection = getSql2o().open()) {
            deleteTableIfExist("country", connection);
            deleteTableIfExist("timetest", connection);
        } catch (ClassNotFoundException e) {
        }
    }
}
