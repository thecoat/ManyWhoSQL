package com.manywho.services.sql.suites;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.suites.sqlserver.data.DateTimeTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommonTestSuite.class,
        DateTimeTest.class
})
public class SqlServerTestSuite {
    @BeforeClass
    public static void setUp() {
        DbConfigurationTest.setPorperties("sqlserver");
    }
}
