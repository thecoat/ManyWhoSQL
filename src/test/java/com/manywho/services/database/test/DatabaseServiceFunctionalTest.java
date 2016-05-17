package com.manywho.services.database.test;

import com.manywho.sdk.test.FunctionalTest;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import javax.ws.rs.core.Application;

public class DatabaseServiceFunctionalTest extends FunctionalTest {

    @Override
    protected Application configure(){

        return new com.manywho.services.database.Application().register(new AbstractBinder() {
            @Override
            protected void configure() {
            }
        });
    }
}
