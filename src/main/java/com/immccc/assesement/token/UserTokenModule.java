package com.immccc.assesement.token;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.immccc.assesement.http.HttpRouteProvider;

public class UserTokenModule extends AbstractModule {
    public static final String USER_TOKEN_ACTOR = "USER_TOKEN_ACTOR";

    @Override
    protected void configure() {

        bind(SimpleAsyncTokenService.class).asEagerSingleton();
        bind(UserTokenHelper.class).asEagerSingleton();
        bind(UserTokenRouteProvider.class).asEagerSingleton();

        Multibinder<HttpRouteProvider> httpRoutesBinder =
                Multibinder.newSetBinder(binder(), HttpRouteProvider.class);
        httpRoutesBinder
                .addBinding()
                .to(UserTokenRouteProvider.class);
    }

    @Provides
    @Singleton
    @Named(USER_TOKEN_ACTOR)
    ActorRef userTokenActor(ActorSystem actorSystem, UserTokenHelper userTokenHelper) {
        return actorSystem.actorOf(
                Props.create(UserTokenActor.class,
                        () -> new UserTokenActor(userTokenHelper)));
    }
}
