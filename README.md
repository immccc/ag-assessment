#Addison Global Assesement implementation
- *Requirements link: https://github.com/addisonglobal/backend-technical-test* 

Please note that, although I received positive feedback from this solution, I was rejected from the position I was applying because I have no
working experience with Scala. 

## How to run/test
- ./gradlew (clean) run <- Linux
- gradlew.bat (clean) run <- Windows
- ./gradlew (clean) test 

Note that I made this project on Windows since my laptop model has a WIFI driver issue in Linux.


## Used technologies - frameworks

### Akka
Although my strong point is Spring related, I decided to give a try to
Akka because Actor Model fits very well to requested asynchronicity.
And anyways, seems it is important for the position I am applying :).

Akka Http provides an experimental bootstrapring mechanism that reduces a lot
the traditional boilerplate code required to run an actor system. However, it is 
easy to fall into a _lambda nesting hell_ when defining endpoints.
I prefer Spring REST because it does not have this pitfall, but I think there is
not many nested lambdas when defining http route, though.

### Guice
A little Dependency Injection framework that helps defining components module by module, so that actor
creation, actor dependencies, etc. are not messing neither _main_ method nor actor nor anything else.  

## Approach to exercise 1 (Sync/Async services)
You can find both implementations (abstract classes) in:
- com.immccc.assesement.token.SyncTokenService
- com.immccc.assesement.token.AsyncTokenService

The former is intended to run authenticate, wait until it finishes,
and then issue the token. Calling thread is blocked until the operation
finishes. 

The latter, instead, creates two nested CompletionStages (CompletionStage
because CompletableFuture is an actual implementor, so better allow abstract methods to decide
which kind of CompletionStage returns). Those are run separately and, therefore, the caller thread
is not blocked. 

## Approach to exercise 2 (Async service implementation)
**NOTE:** *Specification says that this API is different
from the one of the previous exercise. However, this can still be defined in terms of
authenticateUser & issueToken from abstract methods from AsyncTokenService. I think 
spec means to not implement these methods directly in AsyncTokenService, but I am not sure if I misunderstood.
Please let me know.* 

SimpleAsyncTokenService extendsAsyncTokenService.

It calls registered actors to request authentications and token issuing.

## Approach to exercise 3 (REST API)
As provided functionality is token issuing/creation, I am providing a POST method for that.
Not idempotent for sure, since it is suppose to create a new token every time the method is called.
Bear in mind that timestamp varies on each call).

- POST localhost:8080/users/tokens, with Credentials as JSON payload
- curl: 
```
curl -X POST \
  http://localhost:8080/users/tokens \
  -H 'content-type: application/json' \
  -d '{
	"username": "hola",
	"password": "HOLA"
}'
```
