package com.manywho.services.sql.suites;

import com.manywho.services.sql.services.PrimaryKeyService;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PrimaryKeyService.class
})
public class UnitTestSuite {
}
