package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.application.vacancy.VacancyUseCase
import nl.runnable.gigmatch.commands.OpenVacancy
import nl.runnable.gigmatch.commands.toDomainCounterpart
import nl.runnable.gigmatch.domain.vacancy.Job
import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.Term
import nl.runnable.gigmatch.domain.vacancy.VacancyId
import nl.runnable.gigmatch.framework.COMMAND_HANDLER_SUFFIX
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
internal class VacancyCommandHandlers {
    @Bean("nl.runnable.gigmatch.commands.OpenVacancy$COMMAND_HANDLER_SUFFIX")
    fun handleOpenVacancy(useCase: VacancyUseCase) = Consumer<OpenVacancy> { command ->
        useCase.openVacancy(command.toDomainParams())
    }
}

private fun OpenVacancy.toDomainParams() =
    VacancyUseCase.OpenVacancyParams(
        VacancyId(id),
        Job(jobTitle, experience.map { it.toDomainCounterpart() }.toSet()),
        Term(start, end),
        Rate(rateAmount, rateType.toDomainCounterpart()),
        deadline,
    )
