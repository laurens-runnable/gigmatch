package nl.runnable.gigmatch.website;

import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.website.events.EventHandlerResolver;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class MatchEventConsumer {

    private final EventHandlerResolver eventHandlerResolver;

    public MatchEventConsumer(@NonNull EventHandlerResolver eventHandlerResolver) {
        this.eventHandlerResolver = eventHandlerResolver;
    }

    @Incoming("match-events")
    CompletionStage<Void> consumeMessage(Message<byte[]> message) {
        final var metadata = KafkaMetadataUtil.readIncomingKafkaMetadata(message).orElseThrow();

        final var headerValue = metadata.getHeaders().lastHeader("gm.type").value();
        final var eventType = new String(headerValue, StandardCharsets.UTF_8);

        if (!eventHandlerResolver.canHandle(eventType)) {
            if (log.isDebugEnabled()) {
                log.debug("Cannot handle event '{}'|{}", eventType, metadata.getOffset());
            }
            return CompletableFuture.completedStage(null).thenRun(message::ack);
        }

        if (log.isDebugEnabled()) {
            log.debug("Handling event '{}'|{}", eventType, metadata.getOffset());
        }
        final var eventHandler = eventHandlerResolver.resolveEventHandler(eventType);
        final var payload = eventHandlerResolver.deserializePayload(eventType, message.getPayload());
        return eventHandler.handleEvent(payload).thenRun(message::ack);
    }

}
