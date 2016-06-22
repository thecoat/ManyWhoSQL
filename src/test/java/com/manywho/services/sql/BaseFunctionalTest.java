package com.manywho.services.sql;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.manywho.sdk.services.jaxrs.resolvers.ObjectMapperContextResolver;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.managers.DescribeManager;
import com.manywho.services.sql.managers.MetadataManager;
import com.manywho.services.sql.services.*;
import com.manywho.services.sql.types.RawTypeProvider;
import com.manywho.services.sql.utilities.MobjectUtil;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Objects;

public abstract class BaseFunctionalTest extends JerseyTest {

    private Sql2o sql2o;
    protected String portForTest;
    protected String databaseTypeForTest;

    @Override
    protected Application configure() {
        TestApplication application = new TestApplication();

        // read from configuration file
        portForTest = "5432";
        databaseTypeForTest = "postgresql";

        application.setModule(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MetadataManager.class);
                bind(DescribeService.class);
                bind(DescribeManager.class);
                bind(DataManager.class);
                bind(MetadataService.class);
                bind(DataService.class);
                bind(QueryStrService.class);
                bind(QueryParameterService.class).in(Singleton.class);
                bind(TypeProvider.class).to(RawTypeProvider.class);
                bind(MobjectUtil.class);
            }
        });

        application.initialize();

        return application;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(ObjectMapperContextResolver.class);
    }

    protected Sql2o getSql2o() throws ClassNotFoundException {
        if(sql2o != null) {

            return sql2o;
        } else {
            if(Objects.equals(databaseTypeForTest, "postgresql")) {
                Class.forName("com.mysql.jdbc.Driver");
                sql2o = new Sql2o("jdbc:postgresql://localhost:5432/service-sql", "postgres", "admin");

            }else if(Objects.equals(databaseTypeForTest, "sqlserver")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                sql2o = new Sql2o("jdbc:sqlserver://localhost:1433;databaseName=service-sql", "postgres", "admin");
            }

            return sql2o;
        }
    }

    protected HashMap<String, String> getDefaultRequestReplacements() {
        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("{{port}}", portForTest);
        replacements.put("{{databaseType}}", databaseTypeForTest);

        return  replacements;
    }

    protected void deleteTableIfExist(String tableName, Connection connection){
        try {
            connection.createQuery("DROP TABLE "+tableName).executeUpdate();
        } catch (Exception ex){
        }
    }
}