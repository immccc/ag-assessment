package com.immccc.assesement.user;

import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.credentials.CredentialsHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class UserHelperTest {

    private static final Credentials CREDENTIALS = Credentials.builder()
            .username("username")
            .password("password")
            .build();

    @Mock
    private CredentialsHelper credentialsHelper;

    @InjectMocks
    private UserHelper userHelper;

    @Test
    public void successGetUser() {
        User user = userHelper.getUser(CREDENTIALS);
        assertEquals(CREDENTIALS.getUsername(), user.getUserId());
    }

    @Test(expected = RuntimeException.class)
    public void failGetUser() {
        doThrow(RuntimeException.class).when(credentialsHelper).assertValidCredentials(CREDENTIALS);
        userHelper.getUser(CREDENTIALS);
    }
}
