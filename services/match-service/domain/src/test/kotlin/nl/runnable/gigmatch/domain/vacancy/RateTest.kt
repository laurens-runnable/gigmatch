package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class RateTest {
    @ParameterizedTest
    @EnumSource(Rate.Type::class)
    fun `supports JSON serialization`(rateType: Rate.Type) {
        val rate = Rate(100, rateType)
        val objectMapper = ObjectMapper()

        val json = objectMapper.writeValueAsString(rate)
        val deserialized = objectMapper.readValue<Rate>(json)

        deserialized shouldBeEqualTo rate
    }

    @ParameterizedTest
    @EnumSource(Rate.Type::class)
    fun `cannot be instantiated with an amount less than 0`(rateType: Rate.Type) {
        invoking {
            Rate(-1, rateType)
        } shouldThrow IllegalArgumentException::class
    }
}
