package nl.runnable.gigmatch.app.testset

import nl.runnable.gigmatch.events.VacanciesReset
import nl.runnable.gigmatch.framework.axon.RepositoryHelper
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
@TestSetProfile
class VacancyTestSet {

    @Autowired
    private lateinit var repositoryHelper: RepositoryHelper

    @Autowired
    private lateinit var streamBridge: StreamBridge

    fun reset() {
        repositoryHelper.clearRepositories()

        streamBridge.send(MATCH_EVENTS, TypedMessage(VacanciesReset()))
    }
}
