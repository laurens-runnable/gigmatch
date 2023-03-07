package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class SkillTest {
    @Test
    fun `supports JSON serialization`() {
        val skill = Skill(SkillId.generateRandom(), "Kotlin", "kotlin")
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(skill)
        val deserialized = objectMapper.readValue<Skill>(json)

        deserialized shouldBeEqualTo skill
    }

    @Test
    fun `cannot be instantiated with an empty name`() {
        invoking {
            Skill(SkillId.generateRandom(), "", "test")
        } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun `cannot be instantiated with an empty slug`() {
        invoking {
            Skill(SkillId.generateRandom(), "Test", "")
        } shouldThrow IllegalArgumentException::class
    }
}
