package nl.runnable.gigmatch.domain

import org.amshove.kluent.AnyException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.temporal.ChronoUnit

class TimeOffsetTest {

    @ParameterizedTest
    @EnumSource(ChronoUnit::class, names = ["SECONDS", "MINUTES", "HOURS"])
    fun `can be instantiated with time-based units`(unit: ChronoUnit) {
        invoking {
            TimeOffset(1, unit)
        } shouldNotThrow AnyException
    }

    @ParameterizedTest
    @EnumSource(ChronoUnit::class, names = ["DAYS", "WEEKS", "MONTHS", "YEARS"])
    fun `cannot be instantiated with date-based units`(unit: ChronoUnit) {
        invoking {
            TimeOffset(1, unit)
        } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun `can describe if it is positive or negative`() {
        TimeOffset(1, ChronoUnit.MINUTES).isPositive.shouldBeTrue()
        TimeOffset(-1, ChronoUnit.MINUTES).isPositive.shouldBeFalse()

        TimeOffset(-1, ChronoUnit.MINUTES).isNegative.shouldBeTrue()
        TimeOffset(1, ChronoUnit.MINUTES).isNegative.shouldBeFalse()
    }
}
