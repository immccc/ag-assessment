package com.immccc.assesement.user;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class UserModule extends AbstractModule {

    public static final String USER_ACTOR = "USER_ACTOR";

    @Override
    protected void configure() {
        bind(UserHelper.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    @Named(USER_ACTOR)
    ActorRef userActor(ActorSystem actorSystem, UserHelper userHelper) {
        return actorSystem.actorOf(
                Props.create(UserActor.class,
                        () -> new UserActor(userHelper)));
    }
}
