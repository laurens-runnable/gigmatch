package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.CreateVacancy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VacancyInputPort : VacancyUseCase {

    @Autowired
    private lateinit var outputPort: VacancyOutputPort

    override fun createVacancy(params: VacancyUseCase.CreateVacancyParams) {
        outputPort.send(params.toCommand())
    }
}

private fun VacancyUseCase.CreateVacancyParams.toCommand() = CreateVacancy(id, jobTitle, start)
