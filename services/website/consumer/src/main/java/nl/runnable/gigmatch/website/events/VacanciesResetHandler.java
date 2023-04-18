package nl.runnable.gigmatch.website.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import nl.runnable.gigmatch.events.VacanciesReset;
import nl.runnable.gigmatch.model.VacancyRepository;

import java.util.concurrent.CompletionStage;

import static nl.runnable.gigmatch.website.events.EventConstants.HANDLER_SUFFIX;

@ApplicationScoped
@Named("nl.runnable.gigmatch.events.VacanciesReset" + HANDLER_SUFFIX)
@Slf4j
public class VacanciesResetHandler implements EventHandler<VacanciesReset> {

    @Inject
    VacancyRepository vacancyRepository;

    @Override
    public CompletionStage<Void> handleEvent(VacanciesReset event) {
        if (log.isDebugEnabled()) {
            log.debug("Deleting vacancy index");
        }
        return vacancyRepository.deleteAllVacancies();
    }
}
