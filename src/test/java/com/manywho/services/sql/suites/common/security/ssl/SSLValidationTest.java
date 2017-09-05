package com.manywho.services.sql.suites.common.security.ssl;

import com.manywho.services.sql.DbConfigurationTest;
import com.manywho.services.sql.ServiceFunctionalTest;
import com.manywho.services.sql.utilities.DefaultApiRequest;
import org.junit.Test;

public class SSLValidationTest extends ServiceFunctionalTest {

    /**
     * Todo This test should be improved, and the type of exception should be removed form the message
     * the functional test are not currently
     * java.lang.RuntimeException:
     *
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void testNotAllowedSSLWithoutCertificate() throws Exception {
        DbConfigurationTest.setPropertiesIfNotInitialized("postgresql");

        DefaultApiRequest.loadDataRequestAndAssertion("/metadata",
                "suites/common/security/no-certificate-ssl-request.json",
                configurationParameters(),
                "suites/common/security/no-certificate-ssl-response.json",
                dispatcher
        );
    }

}
