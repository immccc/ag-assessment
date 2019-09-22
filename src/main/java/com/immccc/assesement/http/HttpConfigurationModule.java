package com.immccc.assesement.http;

import com.google.inject.AbstractModule;

public class HttpConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HttpBootstraper.class).asEagerSingleton();
    }
}
