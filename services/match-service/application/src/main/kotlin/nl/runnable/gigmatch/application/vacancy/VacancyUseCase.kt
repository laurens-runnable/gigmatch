package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.Job
import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.Term
import nl.runnable.gigmatch.domain.vacancy.VacancyId
import java.time.LocalDate

interface VacancyUseCase {

    class CreateVacancyParams(
        val id: VacancyId,
        val job: Job,
        val term: Term,
        val rate: Rate,
        val deadline: LocalDate,
        val listed: Boolean,
    )

    fun createVacancy(params: CreateVacancyParams)
}
