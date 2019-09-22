package com.immccc.assesement.token;

import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.user.User;

import java.util.concurrent.CompletionStage;

abstract class AsyncTokenService {

    protected abstract CompletionStage<User> authenticate(Credentials credentials);

    protected abstract CompletionStage<UserToken> issueToken(User user);

    public CompletionStage<UserToken> requestToken(Credentials credentials) {
        return authenticate(credentials)
                .thenComposeAsync(this::issueToken);
    }
}
