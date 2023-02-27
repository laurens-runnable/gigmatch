package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.modelling.command.TargetAggregateIdentifier

class CloseVacancy(
    @TargetAggregateIdentifier
    val id: VacancyId,
)
