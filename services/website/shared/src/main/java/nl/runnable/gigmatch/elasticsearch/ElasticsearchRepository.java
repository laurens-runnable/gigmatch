package nl.runnable.gigmatch.elasticsearch;

import io.vertx.core.json.JsonObject;
import lombok.NonNull;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ElasticsearchRepository {

    private final RestClient restClient;

    private final Logger log;

    protected ElasticsearchRepository(@NonNull RestClient restClient) {
        this.restClient = restClient;
        log = LoggerFactory.getLogger(getClass());
    }

    protected final void performRequest(@NonNull Request request) {
        performRequest(request, null);
    }

    protected final void performRequest(@NonNull Request request, Object entity) {
        if (entity != null) {
            final var json = JsonObject.mapFrom(entity).toString();
            request.setJsonEntity(json);
        }

        try {
            restClient.performRequest(request);
        } catch (IOException e) {
            log.error("Error performing Elasticsearch request: {}", e.getMessage(), e);
        }

    }
}
