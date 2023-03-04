package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.events.toFrameworkCounterpart
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component
import nl.runnable.gigmatch.domain.vacancy.VacancyCreated as VacancyCreatedDomainEvent
import nl.runnable.gigmatch.events.VacancyCreated as VacancyCreatedSystemEvent

@Component
internal class VacancyEventHandlers {

    @Autowired
    private lateinit var streamBridge: StreamBridge

    @EventHandler
    private fun on(event: VacancyCreatedDomainEvent) {
        streamBridge.send(
            MATCH_EVENTS,
            TypedMessage(
                VacancyCreatedSystemEvent(
                    event.id.toUUID(),
                    event.job.title,
                    event.job.skills.map { it.toUUID() }.toList(),
                    event.term.start,
                    event.term.end,
                    event.rate.amount,
                    event.rate.type.toFrameworkCounterpart(),
                    event.deadline,
                    event.listed,
                ),
            ),
        )
    }
}
