package com.immccc.assesement.http;

import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import scala.collection.JavaConverters;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class HttpBootstraper extends HttpApp {

    private final Set<HttpRouteProvider> httpRouteProviders;

    @Override
    protected Route routes() {
        return route(JavaConverters.collectionAsScalaIterable(
                httpRouteProviders.stream()
                        .map(HttpRouteProvider::getRoute)
                        .collect(Collectors.toList())
        ).toSeq());
    }
}
