package com.immccc.assesement.token;

import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.user.User;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class AsyncTokenServiceTest {

    private static final Credentials CREDENTIALS = Credentials.builder()
            .username("username")
            .password("password")
            .build();
    private static final User USER = User.builder()
            .userId("username")
            .build();
    private static final UserToken USER_TOKEN = UserToken.builder()
            .token("token")
            .build();


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private AsyncTokenService asyncTokenService;

    @Test
    public void successAuthenticate() throws InterruptedException, ExecutionException {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        when(asyncTokenService.authenticate(CREDENTIALS)).thenReturn(
                CompletableFuture.supplyAsync(getAsyncSupplier(USER, countDownLatch)));

        when(asyncTokenService.issueToken(USER)).thenReturn(
                CompletableFuture.supplyAsync(getAsyncSupplier(USER_TOKEN, countDownLatch)));

        CompletableFuture<UserToken> userTokenToBeReturned = asyncTokenService.requestToken(CREDENTIALS).toCompletableFuture();

        countDownLatch.await();

        UserToken userToken = userTokenToBeReturned.get();
        assertNotNull(userToken);
    }

    @Test(expected = ExecutionException.class)
    public void failAuthenticateOnCredentials() throws InterruptedException, ExecutionException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        when(asyncTokenService.authenticate(CREDENTIALS)).thenReturn(CompletableFuture.supplyAsync(getAsyncFailingSupplier(countDownLatch)));

        when(asyncTokenService.issueToken(USER)).thenReturn(
                CompletableFuture.supplyAsync(getAsyncSupplier(USER_TOKEN, countDownLatch)));

        CompletableFuture<UserToken> userTokenToBeReturned = asyncTokenService.requestToken(CREDENTIALS)
                .toCompletableFuture();

        countDownLatch.await();
        userTokenToBeReturned.get();
    }


    @Test(expected = ExecutionException.class)
    public void failAuthenticateOnIssueToken() throws InterruptedException, ExecutionException {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        when(asyncTokenService.authenticate(CREDENTIALS)).thenReturn(
                CompletableFuture.supplyAsync(getAsyncSupplier(USER, countDownLatch)));

        when(asyncTokenService.issueToken(USER)).thenReturn(
                CompletableFuture.supplyAsync(getAsyncFailingSupplier(countDownLatch)));

        CompletableFuture<UserToken> userTokenToBeReturned = asyncTokenService.requestToken(CREDENTIALS)
                .toCompletableFuture();

        countDownLatch.await();
        userTokenToBeReturned.get();

    }

    private <U> Supplier<U> getAsyncSupplier(U entityToReturn, CountDownLatch countDownLatch) {
        return () -> {
            countDownLatch.countDown();
            return entityToReturn;
        };
    }

    @SuppressWarnings("unused")
    private <U> Supplier<U> getAsyncFailingSupplier(CountDownLatch countDownLatch) {
        return () -> {
            countDownLatch.countDown();
            throw new RuntimeException();
        };
    }
}
