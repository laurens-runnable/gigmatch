package nl.runnable.gigmatch.website.events;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.elasticsearch.ElasticsearchRepository;
import nl.runnable.gigmatch.model.TestSetup;
import nl.runnable.gigmatch.events.TestSetupStarted;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.util.function.Consumer;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@Singleton
@Named("nl.runnable.gigmatch.events.TestSetupStarted" + HANDLER_SUFFIX)
@Slf4j
public class TestSetupStartedHandler extends ElasticsearchRepository implements Consumer<TestSetupStarted> {

    public TestSetupStartedHandler(@NonNull RestClient restClient) {
        super(restClient);
    }

    @Override
    public void accept(TestSetupStarted event) {
        final var testSetup = new TestSetup(event.getId(), false);
        final var endpoint = "/test-setup/_doc/%s".formatted(testSetup.getId());
        final var request = new Request("PUT", endpoint);
        performRequest(request, testSetup);
    }

}
