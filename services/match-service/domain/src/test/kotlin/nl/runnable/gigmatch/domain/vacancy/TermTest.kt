package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TermTest {
    @Test
    fun `supports JSON serialization`() {
        val term = Term(LocalDate.now(), LocalDate.now().plusMonths(1))
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())

        val json = objectMapper.writeValueAsString(term)
        val deserialized = objectMapper.readValue<Term>(json)

        deserialized shouldBeEqualTo term
    }

    @Test
    fun `cannot be instantiated with invalid date range`() {
        invoking {
            Term(LocalDate.now(), LocalDate.now().minusDays(1))
        } shouldThrow IllegalArgumentException::class
    }
}
