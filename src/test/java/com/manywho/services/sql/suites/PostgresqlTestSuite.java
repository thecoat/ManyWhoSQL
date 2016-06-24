package com.manywho.services.sql.suites;

import com.manywho.services.sql.suites.common.controllers.data.DateTimeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DateTimeTest.class,
})
public class PostgresqlTestSuite {
}
