package nl.runnable.gigmatch.elasticsearch;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import nl.runnable.gigmatch.model.Vacancy;
import nl.runnable.gigmatch.model.VacancyRepository;
import org.elasticsearch.client.Request;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ElasticsearchVacancyRepository extends ElasticsearchClient implements VacancyRepository {

    @Override
    public CompletionStage<Void> saveVacancy(@NonNull Vacancy vacancy) {
        final var request = new Request("PUT", "/vacancy/_doc/%s".formatted(vacancy.getId()));
        return performRequestAsyncVoid(request, vacancy);
    }

    @Override
    public CompletionStage<Void> deleteAllVacancies() {
        final var request = new Request("DELETE", "/vacancy");
        return performRequestAsyncVoid(request).exceptionally(e -> null);
    }
}
