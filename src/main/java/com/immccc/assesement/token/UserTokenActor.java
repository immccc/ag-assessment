package com.immccc.assesement.token;

import akka.actor.AbstractActor;
import akka.actor.Status;
import com.google.inject.Inject;
import com.immccc.assesement.user.User;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
class UserTokenActor extends AbstractActor {

    private final UserTokenHelper userTokenHelper;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(User.class,
                this::getAndReturnUserToken)
                .build();
    }

    private void getAndReturnUserToken(User user) {
        try {
            UserToken userToken = userTokenHelper.generateDelayedToken(user, ZonedDateTime.now(ZoneOffset.UTC));
            getSender().tell(userToken, getSelf());
        } catch (Exception e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }

    }
}
