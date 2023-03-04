package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.application.vacancy.VacancyUseCase
import nl.runnable.gigmatch.commands.CreateVacancy
import nl.runnable.gigmatch.commands.toDomainCounterpart
import nl.runnable.gigmatch.domain.vacancy.Job
import nl.runnable.gigmatch.domain.vacancy.Rate
import nl.runnable.gigmatch.domain.vacancy.SkillId
import nl.runnable.gigmatch.domain.vacancy.Term
import nl.runnable.gigmatch.domain.vacancy.VacancyId
import nl.runnable.gigmatch.framework.COMMAND_HANDLER_SUFFIX
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
internal class VacancyCommandHandlers {
    @Bean("nl.runnable.gigmatch.commands.CreateVacancy$COMMAND_HANDLER_SUFFIX")
    fun createVacancy(useCase: VacancyUseCase) = Consumer<CreateVacancy> { command ->
        with(command) {
            useCase.createVacancy(command.toDomainParams())
        }
    }
}

private fun CreateVacancy.toDomainParams() =
    VacancyUseCase.CreateVacancyParams(
        VacancyId(id),
        Job(jobTitle, setOf(SkillId(skillId))),
        Term(start, end),
        Rate(rateAmount, rateType.toDomainCounterpart()),
        deadline,
        listed,
    )
