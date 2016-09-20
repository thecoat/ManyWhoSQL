package com.manywho.services.sql.suites;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.suites.common.controllers.data.LoadTest;
import com.manywho.services.sql.suites.common.controllers.data.MultipleKeyTest;
import com.manywho.services.sql.suites.common.controllers.data.SaveTest;
import com.manywho.services.sql.suites.common.controllers.describe.DescribeTest;
import com.manywho.services.sql.suites.postgresql.data.DateTimeTest;
import com.manywho.services.sql.suites.postgresql.data.CapitalLetterTest;
import com.manywho.services.sql.suites.postgresql.data.UuidTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // common
        LoadTest.class,
        MultipleKeyTest.class,
        SaveTest.class,
        DescribeTest.class,
        //postgresql
        DateTimeTest.class,
        UuidTest.class,
        CapitalLetterTest.class
})
public class PostgreSqlTestSuite {
    @BeforeClass
    public static void setUp() {
        DbConfigurationTest.setPorperties("postgresql");
    }
}
