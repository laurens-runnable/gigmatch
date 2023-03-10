package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class Experience @JsonCreator constructor(
    @JsonProperty("skillId")
    val skillId: SkillId,
    @JsonProperty("level")
    val level: Level,
) {

    enum class Level {
        JUNIOR, MEDIOR, SENIOR
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Experience

        if (skillId != other.skillId) return false

        return true
    }

    override fun hashCode(): Int {
        return skillId.hashCode()
    }
}
