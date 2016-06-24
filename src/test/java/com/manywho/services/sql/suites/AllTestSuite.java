package com.manywho.services.sql.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MySqlTestSuite.class,
        PostgreSqlTestSuite.class,
        SqlServerTestSuite.class
})
public class AllTestSuite {
}
