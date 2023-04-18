package nl.runnable.gigmatch.elasticsearch;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import nl.runnable.gigmatch.model.TestSetup;
import nl.runnable.gigmatch.model.TestSetupRepository;
import org.elasticsearch.client.Request;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
class ElasticsearchTestSetupRepository extends ElasticsearchClient implements TestSetupRepository {

    @Override
    public CompletionStage<Void> saveTestSetup(@NonNull TestSetup testSetup) {
        final var endpoint = "/test-setup/_doc/%s".formatted(testSetup.getId());
        final var request = new Request("PUT", endpoint);
        return performRequestAsyncVoid(request, testSetup);
    }
}
