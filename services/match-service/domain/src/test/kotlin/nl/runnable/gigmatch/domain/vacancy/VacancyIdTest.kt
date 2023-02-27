package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class VacancyIdTest {

    @Test
    fun `can be generated with a random UUID`() {
        VacancyId.generateRandom()
    }

    @Test
    fun `supports JSON serialization`() {
        val vacancyId = VacancyId.generateRandom()
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(vacancyId)
        json shouldBeEqualTo "\"$vacancyId\""

        val deserialized = objectMapper.readValue<VacancyId>(json)
        deserialized shouldBeEqualTo vacancyId
    }
}
