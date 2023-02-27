package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.SkillId
import nl.runnable.gigmatch.domain.vacancy.VacancyId
import java.time.LocalDate

interface VacancyUseCase {

    class CreateVacancyParams(
        val id: VacancyId,
        val jobTitle: String,
        val skillId: SkillId,
        val start: LocalDate,
    )

    fun createVacancy(params: CreateVacancyParams)
}
