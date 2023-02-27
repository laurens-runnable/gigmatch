package nl.runnable.gigmatch.app.testset

import nl.runnable.gigmatch.events.TestSetupCompleted
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component
import java.util.*

@Component
@Endpoint(id = "test-setup-complete")
@TestSetProfile
class TestSetupCompleteEndpoint {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TestSetupCompleteEndpoint::class.java)
    }

    @Autowired
    private lateinit var streamBridge: StreamBridge

    @WriteOperation
    fun end(id: UUID) {
        logger.info("Completed test setup {}", id)
        streamBridge.send(MATCH_EVENTS, TypedMessage(TestSetupCompleted(id)))
    }
}
