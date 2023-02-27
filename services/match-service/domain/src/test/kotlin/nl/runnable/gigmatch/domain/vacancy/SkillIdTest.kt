package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class SkillIdTest {

    @Test
    fun `can be generated with a random UUID`() {
        SkillId.generateRandom()
    }

    @Test
    fun `supports JSON serialization`() {
        val skillId = SkillId.generateRandom()
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(skillId)
        json shouldBeEqualTo "\"$skillId\""

        val deserialized = objectMapper.readValue<SkillId>(json)
        deserialized shouldBeEqualTo skillId
    }
}
