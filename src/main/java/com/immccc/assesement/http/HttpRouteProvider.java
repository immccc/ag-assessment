package com.immccc.assesement.http;

import akka.http.javadsl.server.Route;

public interface HttpRouteProvider {
    Route getRoute();
}
