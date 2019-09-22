package com.immccc.assesement.token;

import com.immccc.assesement.user.User;
import com.immccc.assesement.user.UserHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class UserTokenHelperTest {

    private static final User USER = User.builder()
            .userId("username")
            .build();

    @Mock
    private UserHelper userHelper;

    @InjectMocks
    private UserTokenHelper userTokenHelper;

    @Test
    public void successGenerateToken() {
        ZonedDateTime currentTimestamp = ZonedDateTime.now(ZoneOffset.UTC);
        UserToken userToken = userTokenHelper.generateToken(USER, currentTimestamp);
        assertEquals(USER.getUserId() + "_" + currentTimestamp, userToken.getToken());
    }

    @Test(expected = RuntimeException.class)
    public void failGenerateToken() {
        ZonedDateTime currentTimestamp = ZonedDateTime.now(ZoneOffset.UTC);

        doThrow(RuntimeException.class).when(userHelper).assertBannedUser(USER);
        userTokenHelper.generateToken(USER, currentTimestamp);
    }
}
