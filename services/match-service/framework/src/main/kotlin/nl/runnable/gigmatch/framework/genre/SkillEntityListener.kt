package nl.runnable.gigmatch.framework.genre

import nl.runnable.gigmatch.events.SkillCreatedOrUpdated
import nl.runnable.gigmatch.events.SkillDeleted
import nl.runnable.gigmatch.framework.messaging.MATCH_EVENTS
import nl.runnable.gigmatch.framework.messaging.TypedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Component
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

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
