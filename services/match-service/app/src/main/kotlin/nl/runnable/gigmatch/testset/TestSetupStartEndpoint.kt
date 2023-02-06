package nl.runnable.gigmatch.testset

import nl.runnable.gigmatch.events.TestSetupStarted
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
@Endpoint(id = "test-setup-start")
@TestSetProfile
class TestSetupStartEndpoint {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TestSetupStartEndpoint::class.java)
    }

    @Autowired
    private lateinit var streamBridge: StreamBridge

    @WriteOperation
    fun start(id: UUID) {
        logger.info("Started test setup {}", id)
        streamBridge.send(MATCH_EVENTS, TypedMessage(TestSetupStarted(id)))
    }

}
