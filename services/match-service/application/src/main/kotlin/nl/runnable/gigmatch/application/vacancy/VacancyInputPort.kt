package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.CreateVacancy
import javax.inject.Inject
import javax.inject.Named

@Named
class VacancyInputPort : VacancyUseCase {

    @Inject
    private lateinit var outputPort: VacancyOutputPort

    override fun createVacancy(params: VacancyUseCase.CreateVacancyParams) {
        outputPort.send(params.toCommand())
    }
}

private fun VacancyUseCase.CreateVacancyParams.toCommand() = CreateVacancy(id, jobTitle, start)
