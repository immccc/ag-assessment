package com.immccc.assesement.user;

import com.google.inject.Inject;
import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.credentials.CredentialsHelper;
import com.immccc.assesement.delay.DelayUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserHelper {
    private final CredentialsHelper credentialsHelper;

    public User getDelayedUser(Credentials credentials) {
        DelayUtils.delayRandomly();
        return getUser(credentials);
    }

    public User getUser(Credentials credentials) {
        credentialsHelper.assertValidCredentials(credentials);

        return User.builder()
                .userId(credentials.getUsername())
                .build();
    }

    public void assertBannedUser(User user) {
        boolean isBannedUser = user.getUserId().startsWith("A");
        if (isBannedUser) {
            throw new BannedUserException();
        }
    }
}
