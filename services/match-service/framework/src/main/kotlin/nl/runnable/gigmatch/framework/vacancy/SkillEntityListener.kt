package nl.runnable.gigmatch.framework.vacancy

import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import nl.runnable.gigmatch.events.SkillCreatedOrUpdated
import nl.runnable.gigmatch.events.SkillDeleted
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component

@Component
internal class SkillEntityListener {

    @Autowired
    private lateinit var streamBridge: StreamBridge

    @PostPersist
    @PostUpdate
    fun genreCreatedOrUpdated(skillEntity: SkillEntity) {
        with(skillEntity) {
            streamBridge.send(MATCH_EVENTS, TypedMessage(SkillCreatedOrUpdated(id, name, slug)))
        }
    }

    @PostRemove
    fun genreRemoved(skillEntity: SkillEntity) {
        with(skillEntity) {
            streamBridge.send(MATCH_EVENTS, TypedMessage(SkillDeleted(id)))
        }
    }
}
