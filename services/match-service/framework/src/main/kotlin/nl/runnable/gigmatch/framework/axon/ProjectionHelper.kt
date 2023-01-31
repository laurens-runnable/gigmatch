package nl.runnable.gigmatch.framework.axon

import org.axonframework.config.Configuration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProjectionHelper {

    @Autowired
    private lateinit var configuration: Configuration

    fun resetProjections() {
        trackingEventProcessors().forEach {
            it.shutDown()
            it.resetTokens()
            it.start()
        }
    }

    private fun trackingEventProcessors(): List<TrackingEventProcessor> {
        return configuration.eventProcessingConfiguration().eventProcessors()
            .map { it.value as TrackingEventProcessor }
    }

}
