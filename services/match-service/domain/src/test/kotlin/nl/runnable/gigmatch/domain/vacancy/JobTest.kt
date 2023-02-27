package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class JobTest {
    @Test
    fun `supports JSON serialization`() {
        val job = Job("Java developer", setOf(SkillId.generateRandom()))
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(job)
        val deserialized = objectMapper.readValue<Job>(json)

        deserialized shouldBeEqualTo job
    }
}
