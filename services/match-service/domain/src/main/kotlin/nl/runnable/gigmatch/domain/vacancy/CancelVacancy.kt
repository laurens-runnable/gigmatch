package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.modelling.command.TargetAggregateIdentifier

class CancelVacancy(
    @TargetAggregateIdentifier
    val id: VacancyId,
)
