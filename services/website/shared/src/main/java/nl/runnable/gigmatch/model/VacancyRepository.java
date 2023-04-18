package nl.runnable.gigmatch.model;

import lombok.NonNull;

import java.util.concurrent.CompletionStage;

public interface VacancyRepository {

    CompletionStage<Void> saveVacancy(@NonNull Vacancy vacancy);

    CompletionStage<Void> deleteAllVacancies();
}
