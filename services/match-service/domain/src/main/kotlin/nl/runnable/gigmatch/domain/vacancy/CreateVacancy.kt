package nl.runnable.gigmatch.domain.vacancy

import java.time.LocalDate

class CreateVacancy(
    val id: VacancyId,
    val job: Job,
    val start: LocalDate,
)
