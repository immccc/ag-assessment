package com.immccc.assesement.token;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.ExceptionHandler;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import com.google.inject.Inject;
import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.http.HttpRouteProvider;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;


@AllArgsConstructor(onConstructor = @__(@Inject))
class UserTokenRouteProvider extends AllDirectives implements HttpRouteProvider {

    private final SimpleAsyncTokenService tokenService;

    @Override
    public Route getRoute() {
        PathMatcher0 userTokenPathMatcher = PathMatchers.segment("users").slash("tokens");
        return path(userTokenPathMatcher, getSupplierUserTokenRoute());
    }

    private Supplier<Route> getSupplierUserTokenRoute() {
        return () -> post(
                () -> entity(Jackson.unmarshaller(Credentials.class), credentials -> {
                    CompletionStage<UserToken> userTokenToBeReturned =
                            tokenService.requestToken(credentials);

                    return handleExceptions(getSimpleExceptionHandler(), () ->
                            onSuccess(userTokenToBeReturned, userToken ->
                                    completeOK(userToken, Jackson.marshaller())));
                }));
    }

    private ExceptionHandler getSimpleExceptionHandler() {
        return ExceptionHandler.newBuilder()
                .match(
                        RuntimeException.class,
                        exception -> complete(
                                StatusCodes.BAD_REQUEST,
                                "Something seems invalid: " + exception.getClass().getSimpleName()))
                .build();
    }
}