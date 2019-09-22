package com.immccc.assesement;

import akka.actor.ActorSystem;
import com.google.inject.AbstractModule;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActorSystem.class).toInstance(ActorSystem.apply());
    }
}
