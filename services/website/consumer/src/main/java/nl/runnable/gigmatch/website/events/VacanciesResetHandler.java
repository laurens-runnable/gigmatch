package nl.runnable.gigmatch.website.events;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.elasticsearch.ElasticsearchRepository;
import nl.runnable.gigmatch.events.VacanciesReset;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.util.function.Consumer;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@Singleton
@Named("nl.runnable.gigmatch.events.VacanciesReset" + HANDLER_SUFFIX)
@Slf4j
public class VacanciesResetHandler extends ElasticsearchRepository implements Consumer<VacanciesReset> {

    protected VacanciesResetHandler(@NonNull RestClient restClient) {
        super(restClient);
    }

    @Override
    public void accept(VacanciesReset object) {
        performRequest(new Request("DELETE", "/vacancy"));
    }

}
