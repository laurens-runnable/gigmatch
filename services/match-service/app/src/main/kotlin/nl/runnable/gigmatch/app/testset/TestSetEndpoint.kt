package nl.runnable.gigmatch.app.testset

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.stereotype.Component

/**
 * Management endpoint for end-to-end testing.
 */
@Component
@Endpoint(id = "test-set")
@TestSetProfile
class TestSetEndpoint {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TestSetEndpoint::class.java)
    }

    @Autowired
    private lateinit var vacancyTestSet: VacancyTestSet

    /**
     * Resets the test set.
     */
    @WriteOperation
    fun reset() {
        logger.info("Resetting test set")
        vacancyTestSet.reset()
    }
}
