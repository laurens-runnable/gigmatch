package nl.runnable.gigmatch.domain.vacancy

import java.time.LocalDate

class OpenVacancyCommand(
    val id: VacancyId,
    val job: Job,
    val term: Term,
    val rate: Rate,
    val deadline: LocalDate,
)
