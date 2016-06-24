package com.manywho.services.sql.suites;

import com.manywho.services.sql.ConfigurationDB;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommonTestSuite.class
})
public class MySqlTestSuite {
    @BeforeClass
    public static void setUp() {
        ConfigurationDB.setPorperties("mysql");
    }
}
