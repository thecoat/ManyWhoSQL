package com.manywho.services.database;

import com.manywho.sdk.services.BaseApplication;

public class Application extends BaseApplication {

    public Application(){
        registerSdk()
                .packages("com.manywho.services.database")
                .register(new ApplicationBinder());
    }

    public static void main(String[] args) {
        startServer(new Application(), "api/database/1");
    }
}
