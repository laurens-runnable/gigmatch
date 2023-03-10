package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class ExperienceTest {
    @ParameterizedTest
    @EnumSource(Experience.Level::class)
    fun `supports JSON serialization`(level: Experience.Level) {
        val experience = Experience(SkillId.generateRandom(), level)
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(experience)
        val deserialized = objectMapper.readValue<Experience>(json)

        deserialized shouldBeEqualTo experience
    }

    @Test
    fun `is uniquely identified by Skill`() {
        val skillId = SkillId.generateRandom()
        val a = Experience(skillId, Experience.Level.JUNIOR)
        val b = Experience(skillId, Experience.Level.MEDIOR)

        a shouldBeEqualTo b

        val c = Experience(SkillId.generateRandom(), Experience.Level.MEDIOR)

        a shouldNotBeEqualTo c
    }
}
