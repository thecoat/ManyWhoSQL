package com.manywho.services.sql.suites;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.suites.postgresql.data.DateTimeTest;
import com.manywho.services.sql.suites.postgresql.data.UuidTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DateTimeTest.class,
        CommonTestSuite.class,
        UuidTest.class
})
public class PostgreSqlTestSuite {
    @BeforeClass
    public static void setUp() {
        DbConfigurationTest.setPorperties("postgresql");
    }
}
