package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.modelling.command.TargetAggregateIdentifier

class CloseVacancyCommand(
    @TargetAggregateIdentifier
    val id: VacancyId,
)
