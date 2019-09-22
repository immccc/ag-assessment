package com.immccc.assesement.token;

import akka.actor.ActorRef;
import akka.util.Timeout;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.user.User;

import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;
import static com.immccc.assesement.token.UserTokenModule.USER_TOKEN_ACTOR;
import static com.immccc.assesement.user.UserModule.USER_ACTOR;
import static java.util.concurrent.TimeUnit.MINUTES;

class SimpleAsyncTokenService extends AsyncTokenService {

    private static final Timeout TIMEOUT = Timeout.apply(1, MINUTES);

    private final ActorRef userActorRef;
    private final ActorRef userTokenActorRef;

    @Inject
    SimpleAsyncTokenService(
            @Named(USER_ACTOR) ActorRef userActorRef,
            @Named(USER_TOKEN_ACTOR) ActorRef userTokenActorRef) {

        this.userActorRef = userActorRef;
        this.userTokenActorRef = userTokenActorRef;
    }

    @Override
    protected CompletionStage<User> authenticate(Credentials credentials) {
        return ask(userActorRef, credentials, Timeout.apply(1, MINUTES))
                .thenApplyAsync(user -> (User) user);
    }

    @Override
    protected CompletionStage<UserToken> issueToken(User user) {
        return ask(userTokenActorRef, user, TIMEOUT)
                .thenApplyAsync(userToken -> (UserToken) userToken);
    }
}
