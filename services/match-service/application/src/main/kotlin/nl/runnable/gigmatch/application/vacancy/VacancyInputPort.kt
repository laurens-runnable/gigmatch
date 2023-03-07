package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.OpenVacancyCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VacancyInputPort : VacancyUseCase {

    @Autowired
    private lateinit var outputPort: VacancyOutputPort

    override fun openVacancy(params: VacancyUseCase.OpenVacancyParams) {
        outputPort.send(params.toCommand())
    }
}

private fun VacancyUseCase.OpenVacancyParams.toCommand() = OpenVacancyCommand(id, job, term, rate, deadline)
