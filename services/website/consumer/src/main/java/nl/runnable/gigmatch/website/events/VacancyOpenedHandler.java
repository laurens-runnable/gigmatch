package nl.runnable.gigmatch.website.events;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.elasticsearch.ElasticsearchRepository;
import nl.runnable.gigmatch.model.Vacancy;
import nl.runnable.gigmatch.events.VacancyOpened;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.util.Collections;
import java.util.function.Consumer;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@Singleton
@Named("nl.runnable.gigmatch.events.VacancyOpened" + HANDLER_SUFFIX)
@Slf4j
public class VacancyOpenedHandler extends ElasticsearchRepository implements Consumer<VacancyOpened> {

    protected VacancyOpenedHandler(@NonNull RestClient restClient) {
        super(restClient);
    }

    @Override
    public void accept(VacancyOpened event) {
        final var doc = createVacancyDocument(event);
        final var endpoint = "/vacancy/_doc/%s".formatted(doc.getId());
        final var request = new Request("PUT", endpoint);
        performRequest(request, doc);
    }

    private static Vacancy createVacancyDocument(VacancyOpened event) {
        return new Vacancy(event.getId(), event.getJobTitle(), Collections.emptyList());
    }

}
