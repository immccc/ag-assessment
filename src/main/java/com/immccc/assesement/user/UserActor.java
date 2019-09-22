package com.immccc.assesement.user;

import akka.actor.AbstractActor;
import akka.actor.Status;
import com.google.inject.Inject;
import com.immccc.assesement.credentials.Credentials;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserActor extends AbstractActor {
    private final UserHelper userHelper;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Credentials.class,
                this::getAndReturnUser)
                .build();
    }

    private void getAndReturnUser(Credentials credentials) {
        try {
            User user = userHelper.getDelayedUser(credentials);
            getSender().tell(user, getSelf());
        } catch (Exception e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }
}
