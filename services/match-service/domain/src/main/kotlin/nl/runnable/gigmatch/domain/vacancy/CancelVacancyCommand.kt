package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.modelling.command.TargetAggregateIdentifier

class CancelVacancyCommand(
    @TargetAggregateIdentifier
    val id: VacancyId,
)
