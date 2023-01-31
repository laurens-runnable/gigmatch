package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.application.vacancy.VacancyUseCase
import nl.runnable.gigmatch.commands.CreateVacancy
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
            val params = VacancyUseCase.CreateVacancyParams(VacancyId(id), name, start)
            useCase.createVacancy(params)
        }
    }

}
