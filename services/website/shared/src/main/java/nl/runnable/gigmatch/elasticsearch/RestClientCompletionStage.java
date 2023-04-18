package nl.runnable.gigmatch.elasticsearch;

import lombok.NonNull;
import lombok.Setter;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;

import java.util.concurrent.CompletableFuture;

/**
 * Adapts {@link org.elasticsearch.client.RestClient} async operations to a {@link java.util.concurrent.CompletionStage}.
 */
class RestClientCompletionStage extends CompletableFuture<Response> implements ResponseListener {

    @Setter
    @NonNull
    private Cancellable cancellable;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        cancellable.cancel();

        return super.cancel(mayInterruptIfRunning);
    }

    @Override
    public void onSuccess(Response response) {
        complete(response);
    }

    @Override
    public void onFailure(Exception e) {
        completeExceptionally(e);
    }
}
