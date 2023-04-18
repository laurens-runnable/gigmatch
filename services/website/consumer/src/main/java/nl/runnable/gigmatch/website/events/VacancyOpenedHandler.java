package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.events.VacancyOpened;
import nl.runnable.gigmatch.model.Vacancy;
import nl.runnable.gigmatch.model.VacancyRepository;

import java.util.Collections;
import java.util.concurrent.CompletionStage;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@ApplicationScoped
@Named("nl.runnable.gigmatch.events.VacancyOpened" + HANDLER_SUFFIX)
@Slf4j
public class VacancyOpenedHandler implements EventHandler<VacancyOpened> {

    @Inject
    VacancyRepository vacancyRepository;

    @Override
    public CompletionStage<Void> handleEvent(VacancyOpened event) {
        if (log.isDebugEnabled()) {
            log.debug("Saving Vacancy {}", event.getId());
        }
        final var vacancy = toVacancy(event);
        return vacancyRepository.saveVacancy(vacancy);
    }

    private static Vacancy toVacancy(VacancyOpened event) {
        return new Vacancy(event.getId(), event.getJobTitle(), Collections.emptyList());
    }
}
