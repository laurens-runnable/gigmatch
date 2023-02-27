package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import nl.runnable.gigmatch.domain.Id
import java.util.*

class SkillId @JsonCreator constructor(uuid: UUID) : Id(uuid) {

    companion object {
        fun generateRandom(): SkillId = SkillId(UUID.randomUUID())
    }
}
