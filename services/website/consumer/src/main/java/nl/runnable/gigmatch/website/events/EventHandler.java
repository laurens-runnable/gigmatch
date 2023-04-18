package nl.runnable.gigmatch.website.events;

import java.util.concurrent.CompletionStage;

public interface EventHandler<TEvent> {

    CompletionStage<Void> handleEvent(TEvent event);
}
