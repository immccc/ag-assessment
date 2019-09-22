package com.immccc.assesement.credentials;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class CredentialsHelperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    private CredentialsHelper credentialsHelper;

    @Test
    @Parameters({
            "null, password, true",
            "username, null, true",
            "username, username, true",
            "username, USERNAME, false"
    })
    public void assertValidCredentials(String username, String password, boolean exceptionExpected) {
        Credentials credentials = Credentials.builder()
                .username(username)
                .password(password)
                .build();

        try {
            credentialsHelper.assertValidCredentials(credentials);
        } catch (InvalidCredentialsException e) {
            assertTrue(exceptionExpected);
            return;
        }

        assertFalse(exceptionExpected);


    }

}
