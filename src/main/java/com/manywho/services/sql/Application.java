package com.manywho.services.sql;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.manywho.sdk.services.BaseApplication;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.managers.MetadataManager;
import com.manywho.services.sql.managers.DescribeManager;
import com.manywho.services.sql.services.ConnectionService;
import com.manywho.services.sql.services.DataService;
import com.manywho.services.sql.services.DescribeService;
import com.manywho.services.sql.services.MetadataService;

public class Application extends BaseApplication {

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.addModule(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MetadataManager.class).in(Singleton.class);
                bind(DescribeService.class).in(Singleton.class);
                bind(DescribeManager.class).in(Singleton.class);
                bind(ConnectionService.class).in(Singleton.class);
                bind(DataManager.class).in(Singleton.class);
                bind(MetadataService.class).in(Singleton.class);
                bind(DataService.class).in(Singleton.class);
            }
        });

        application.startServer("api/sql/1");
    }
}
