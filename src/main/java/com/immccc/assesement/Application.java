package com.immccc.assesement;

import akka.actor.ActorSystem;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.immccc.assesement.credentials.CredentialsModule;
import com.immccc.assesement.http.HttpBootstraper;
import com.immccc.assesement.http.HttpConfigurationModule;
import com.immccc.assesement.token.UserTokenModule;
import com.immccc.assesement.user.UserModule;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutionException;

public class Application {

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static final int DEFAULT_SERVER_PORT = 8080;

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public static void main(String[] args) {

        Injector injector = Guice.createInjector(
                new CredentialsModule(),
                new UserTokenModule(),
                new UserModule(),
                new ApplicationModule(),
                new HttpConfigurationModule());

        ActorSystem actorSystem = injector.getInstance(ActorSystem.class);
        HttpBootstraper httpBootstraper = injector.getInstance(HttpBootstraper.class);
        httpBootstraper.startServer(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT, actorSystem);

        actorSystem.terminate();


    }
}
