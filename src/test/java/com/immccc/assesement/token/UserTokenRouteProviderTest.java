package com.immccc.assesement.token;

import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.immccc.assesement.credentials.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static akka.http.javadsl.model.ContentTypes.APPLICATION_JSON;
import static akka.http.javadsl.model.HttpRequest.POST;
import static akka.http.javadsl.model.StatusCodes.BAD_REQUEST;
import static akka.http.javadsl.model.StatusCodes.OK;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTokenRouteProviderTest extends JUnitRouteTest {

    private static final Credentials CREDENTIALS = Credentials.builder()
            .username("username")
            .password("password")
            .build();
    private static final UserToken USER_TOKEN = UserToken.builder()
            .token("token")
            .build();

    private static final String URI = "/users/tokens";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private SimpleAsyncTokenService tokenService;

    private TestRoute testRoute;

    private UserTokenRouteProvider userTokenRouteProvider;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userTokenRouteProvider = new UserTokenRouteProvider(tokenService);
        testRoute = getTestRoute();
    }

    @Test
    public void successfulUserTokenPost() throws Exception {

        CompletableFuture<UserToken> userTokenToBeReturned = CompletableFuture.completedFuture(USER_TOKEN);

        when(tokenService.requestToken(any(Credentials.class))).thenReturn(userTokenToBeReturned);

        testRoute.run(POST(URI)
                .withEntity(HttpEntities.create(
                        APPLICATION_JSON, OBJECT_MAPPER.writeValueAsString(CREDENTIALS))))
                .assertEntity(OBJECT_MAPPER.writeValueAsString(USER_TOKEN))
                .assertStatusCode(OK);

    }

    @Test
    public void failedUserTokenPost() throws Exception {

        CompletableFuture<UserToken> userTokenToBeReturned = new CompletableFuture<>();
        userTokenToBeReturned.completeExceptionally(new RuntimeException());

        when(tokenService.requestToken(any(Credentials.class))).thenReturn(userTokenToBeReturned);

        testRoute.run(POST(URI)
                .withEntity(HttpEntities.create(
                        APPLICATION_JSON, OBJECT_MAPPER.writeValueAsString(CREDENTIALS))))
                .assertStatusCode(BAD_REQUEST);
    }

    private TestRoute getTestRoute() {
        return testRoute(userTokenRouteProvider.getRoute());
    }
}
