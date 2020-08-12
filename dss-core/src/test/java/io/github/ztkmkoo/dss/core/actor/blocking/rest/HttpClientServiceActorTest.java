package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 02:23
 */
class HttpClientServiceActorTest extends AbstractDssActorTest {

    @SuppressWarnings("unchecked")
    @Test
    void testHandlingHttpGetRequest() {
        final ActorRef<DssBlockingRestCommand> ref = testKit.spawn(HttpClientServiceActor.create(new HttpClientService<String>() {
            private final Logger logger = LoggerFactory.getLogger(HttpClientServiceActorTest.TestService.class);

            @Override
            public DssBlockingRestCommand.DssHttpResponseCommand<String> getRequest(DssBlockingRestCommand.HttpGetRequest request) {
                logger.info("HttpGetRequest: {}", request);
                return TestResponse
                        .builder()
                        .code(200)
                        .message("OK")
                        .body("Hi")
                        .build();
            }
        }));
        final TestProbe<DssBlockingRestCommand> probe = testKit.createTestProbe();

        final long seq = 33;
        ref.tell(DssBlockingRestCommand.HttpGetRequest
                .builder(seq, probe.getRef(), "https://www.github.com/test")
                .build());

        final DssBlockingRestCommand response = probe.receiveMessage();
        if (response instanceof DssBlockingRestCommand.HttpResponse) {
            final DssBlockingRestCommand.HttpResponse<String> testResponse = (DssBlockingRestCommand.HttpResponse<String>) response;
            assertEquals(33L, testResponse.getSeq());
            assertEquals(ref, testResponse.getSender());
            assertEquals(200, testResponse.getCode());
        } else {
            fail();
        }
    }

    private static class TestService implements HttpClientService<String> {

        private final Logger logger = LoggerFactory.getLogger(TestService.class);

        @Override
        public DssBlockingRestCommand.DssHttpResponseCommand<String> getRequest(DssBlockingRestCommand.HttpGetRequest request) {
            logger.info("HttpGetRequest: {}", request);
            return TestResponse
                    .builder()
                    .code(200)
                    .message("OK")
                    .body("Hi")
                    .build();
        }
    }

    @Getter
    private static class TestResponse implements DssBlockingRestCommand.DssHttpResponseCommand<String> {
        private static final long serialVersionUID = -1320682451871656787L;

        private final int code;
        private final String message;
        private final String body;

        @Builder
        public TestResponse(int code, String message, String body) {
            this.code = code;
            this.message = message;
            this.body = body;
        }
    }
}