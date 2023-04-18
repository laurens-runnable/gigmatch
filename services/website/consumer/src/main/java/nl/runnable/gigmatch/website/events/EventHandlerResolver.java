package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.enterprise.inject.spi.CDI;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;

import java.io.IOException;
import java.util.NoSuchElementException;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@ApplicationScoped
@Slf4j
public class EventHandlerResolver {

    private BeanContainer getBeanContainer() {
        return CDI.current().getBeanContainer();
    }

    private static String eventHandlerBeanName(String eventType) {
        return "%s%s".formatted(eventType, HANDLER_SUFFIX);
    }

    public boolean canHandle(@NonNull String eventType) {
        return getBeanContainer().getBeans(eventHandlerBeanName(eventType)).stream()
                .findFirst()
                .map(bean -> EventHandler.class.isAssignableFrom(bean.getBeanClass()))
                .orElse(false);
    }

    public Object deserializePayload(@NonNull String eventType, byte @NonNull [] input) {
        try {
            @SuppressWarnings("unchecked") final Class<Object> clazz = (Class<Object>) Class.forName(eventType);
            final var reader = new ReflectDatumReader<>(clazz);
            final var binaryDecoder = DecoderFactory.get().binaryDecoder(input, null);
            return reader.read(null, binaryDecoder);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find class '%s'".formatted(eventType), e);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing event payload", e);
        }
    }

    @SuppressWarnings("unchecked")
    public EventHandler<Object> resolveEventHandler(@NonNull String eventType) {
        final String beanName = eventHandlerBeanName(eventType);
        final var beans = getBeanContainer().getBeans(beanName);

        final Bean<?> bean = beans.stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find bean '%s'".formatted(beanName)));
        if (!EventHandler.class.isAssignableFrom(bean.getBeanClass())) {
            throw new NoSuchElementException("Bean '%s' is not of type %s".formatted(beanName, EventHandler.class.getName()));
        }

        final var context = getBeanContainer().createCreationalContext(bean);
        return (EventHandler<Object>) getBeanContainer().getReference(bean, Object.class, context);
    }

}
