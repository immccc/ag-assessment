package com.immccc.assesement.token;

import com.google.inject.Inject;
import com.immccc.assesement.delay.DelayUtils;
import com.immccc.assesement.user.User;
import com.immccc.assesement.user.UserHelper;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class UserTokenHelper {

    private static final String USERID_TOKEN_SEPARATOR = "_";

    private final UserHelper userHelper;

    UserToken generateDelayedToken(User user, ZonedDateTime dateTime) {
        DelayUtils.delayRandomly();
        return generateToken(user, dateTime);
    }

    UserToken generateToken(User user, ZonedDateTime dateTime) {
        userHelper.assertBannedUser(user);

        return UserToken.builder()
                .token(user.getUserId() + USERID_TOKEN_SEPARATOR + dateTime.toString())
                .build();
    }
}
