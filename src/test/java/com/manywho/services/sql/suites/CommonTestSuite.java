package com.manywho.services.sql.suites;

import com.manywho.services.sql.suites.common.controllers.data.LoadTest;
import com.manywho.services.sql.suites.common.controllers.data.MultipleKeyTest;
import com.manywho.services.sql.suites.common.controllers.data.SaveTest;
import com.manywho.services.sql.suites.common.controllers.describe.DescribeTest;
import com.manywho.services.sql.suites.common.services.PrimaryKeyServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoadTest.class,
        MultipleKeyTest.class,
        SaveTest.class,
        DescribeTest.class,
        PrimaryKeyServiceTest.class,
})
public class CommonTestSuite {
}
