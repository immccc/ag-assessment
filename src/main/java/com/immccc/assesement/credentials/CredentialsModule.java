package com.immccc.assesement.credentials;

import com.google.inject.AbstractModule;

public class CredentialsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CredentialsHelper.class).asEagerSingleton();
    }
}
