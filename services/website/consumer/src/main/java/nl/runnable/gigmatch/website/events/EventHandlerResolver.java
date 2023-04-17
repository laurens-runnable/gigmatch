package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@Singleton
public class EventHandlerResolver {

    private BeanContainer getBeanContainer() {
        return CDI.current().getBeanContainer();
    }

    public boolean canHandle(@NonNull String type) {
        final var beanName = "%s%s".formatted(type, HANDLER_SUFFIX);
        final var beans = getBeanContainer().getBeans(beanName);
        return !beans.isEmpty();
    }

    @SneakyThrows
    public Object deserializePayload(@NonNull String type, byte @NonNull [] input) {
        @SuppressWarnings("unchecked") final Class<Object> clazz = (Class<Object>) Class.forName(type);
        final var reader = new ReflectDatumReader<>(clazz);
        final var binaryDecoder = DecoderFactory.get().binaryDecoder(input, null);
        return reader.read(null, binaryDecoder);
    }

    @SuppressWarnings("unchecked")
    public Consumer<Object> resolveEventHandler(@NonNull String type) {
        final var beanName = "%s%s".formatted(type, HANDLER_SUFFIX);
        final var beans = getBeanContainer().getBeans(beanName);

        final Bean<?> bean = beans.stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find bean: %s".formatted(type)));
        final var context = getBeanContainer().createCreationalContext(bean);
        return (Consumer<Object>) getBeanContainer().getReference(bean, Object.class, context);
    }

}
