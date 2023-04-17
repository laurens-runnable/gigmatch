package nl.runnable.gigmatch.website;

import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.website.events.EventHandlerResolver;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.nio.charset.StandardCharsets;
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
        final var type = new String(headerValue, StandardCharsets.UTF_8);

        if (eventHandlerResolver.canHandle(type)) {
            final var eventHandler = eventHandlerResolver.resolveEventHandler(type);
            final var payload = eventHandlerResolver.deserializePayload(type, message.getPayload());
            eventHandler.accept(payload);
        } else {
            log.trace("Cannot handle message of type '{}'", type);
        }

        return message.ack();
    }

}
