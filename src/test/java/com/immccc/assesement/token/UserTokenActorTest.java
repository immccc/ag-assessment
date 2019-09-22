package com.immccc.assesement.token;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.javadsl.TestKit;
import com.immccc.assesement.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZonedDateTime;

import static akka.testkit.javadsl.TestKit.shutdownActorSystem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserTokenActorTest {

    private static final User USER = User.builder()
            .userId("username")
            .build();

    private static final UserToken USER_TOKEN = UserToken.builder()
            .token("usertoken")
            .build();

    @Mock
    private UserTokenHelper userTokenHelper;

    private ActorSystem actorSystem;

    @Before
    public void init() {
        actorSystem = ActorSystem.create();
    }

    @After
    public void tearDown() {
        shutdownActorSystem(actorSystem);
    }

    @Test
    public void userRetrievalSuccess() {

        when(userTokenHelper.generateDelayedToken(
                any(User.class),
                any(ZonedDateTime.class)))
                .thenReturn(USER_TOKEN);

        new TestKit(actorSystem) {{
            ActorRef userActor = getUserActorRef();

            userActor.tell(USER, getRef());
            expectMsg(USER_TOKEN);
        }};
    }

    @Test
    public void userRetrievalFailure() {

        when(userTokenHelper.generateDelayedToken(
                any(User.class),
                any(ZonedDateTime.class)))
                .thenThrow(new RuntimeException());

        new TestKit(actorSystem) {{
            ActorRef userActor = getUserActorRef();

            userActor.tell(USER, getRef());
            expectMsgAnyClassOf(Status.Failure.class);
        }};
    }


    private ActorRef getUserActorRef() {
        return actorSystem.actorOf(
                Props.create(UserTokenActor.class,
                        () -> new UserTokenActor(userTokenHelper)));
    }


}
