package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.application.vacancy.VacancyOutputPort
import nl.runnable.gigmatch.domain.vacancy.OpenVacancyCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
internal class AxonVacancyAdapter : VacancyOutputPort {

    @Autowired
    private lateinit var commandGateway: CommandGateway

    override fun send(command: OpenVacancyCommand) {
        commandGateway.sendAndWait<Void>(command)
    }
}
