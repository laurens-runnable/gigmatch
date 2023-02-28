package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
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

    @Test
    fun `cannot be created with empty Skills`() {
        invoking {
            Job("Java developer", emptySet())
        } shouldThrow IllegalArgumentException::class
    }
}
