package nl.runnable.gigmatch.app.axon

import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.jobrunr.JobRunrEventScheduler
import org.axonframework.serialization.Serializer
import org.jobrunr.scheduling.JobScheduler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventSchedulerConfiguration {
    @Bean
    fun eventScheduler(
        @Qualifier("eventSerializer") serializer: Serializer,
        jobScheduler: JobScheduler,
        eventBus: EventBus,
        transactionManager: TransactionManager,
    ): EventScheduler {
        return JobRunrEventScheduler.builder()
            .jobScheduler(jobScheduler)
            .serializer(serializer)
            .eventBus(eventBus)
            .transactionManager(transactionManager)
            .build()
    }
}
