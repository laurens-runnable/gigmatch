package nl.runnable.gigmatch.elasticsearch;

import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import lombok.NonNull;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.util.concurrent.CompletionStage;

abstract class ElasticsearchClient {

    @Inject
    RestClient restClient;

    protected final CompletionStage<Response> performRequestAsync(@NonNull Request request, Object entity) {
        if (entity != null) {
            final var json = JsonObject.mapFrom(entity).toString();
            request.setJsonEntity(json);
        }

        final var stage = new RestClientCompletionStage();
        final var cancellable = restClient.performRequestAsync(request, stage);
        stage.setCancellable(cancellable);
        return stage;
    }

    protected final CompletionStage<Void> performRequestAsyncVoid(@NonNull Request request) {
        return performRequestAsyncVoid(request, null);
    }

    protected final CompletionStage<Void> performRequestAsyncVoid(@NonNull Request request, Object entity) {
        return performRequestAsync(request, entity).thenRun(() -> {
        });
    }
}
