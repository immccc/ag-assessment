package com.immccc.assesement.user;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.javadsl.TestKit;
import com.immccc.assesement.credentials.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static akka.testkit.javadsl.TestKit.shutdownActorSystem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserActorTest {

    private static final Credentials CREDENTIALS = Credentials.builder()
            .username("username")
            .password("password")
            .build();
    private static final User USER = User.builder()
            .userId("username")
            .build();

    @Mock
    private UserHelper userHelper;

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

        when(userHelper.getDelayedUser(any(Credentials.class)))
                .thenReturn(USER);

        new TestKit(actorSystem) {{
            ActorRef userActor = getUserActorRef();

            userActor.tell(CREDENTIALS, getRef());
            expectMsg(USER);
        }};
    }

    @Test
    public void userRetrievalFailure() {
        when(userHelper.getDelayedUser(any(Credentials.class)))
                .thenThrow(new RuntimeException());

        new TestKit(actorSystem) {{
            ActorRef userActor = getUserActorRef();

            userActor.tell(CREDENTIALS, getRef());
            expectMsgAnyClassOf(Status.Failure.class);
        }};
    }

    private ActorRef getUserActorRef() {
        return actorSystem.actorOf(
                Props.create(UserActor.class,
                        () -> new UserActor(userHelper)));
    }

}
