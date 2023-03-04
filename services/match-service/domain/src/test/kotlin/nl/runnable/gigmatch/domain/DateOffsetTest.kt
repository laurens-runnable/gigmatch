package nl.runnable.gigmatch.domain

import org.amshove.kluent.AnyException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.temporal.ChronoUnit

class DateOffsetTest {

    @ParameterizedTest
    @EnumSource(ChronoUnit::class, names = ["DAYS", "WEEKS", "MONTHS", "YEARS"])
    fun `can be instantiated with date-based units`(unit: ChronoUnit) {
        invoking {
            DateOffset(1, unit)
        } shouldNotThrow AnyException
    }

    @ParameterizedTest
    @EnumSource(ChronoUnit::class, names = ["SECONDS", "MINUTES", "HOURS"])
    fun `cannot be instantiated with time-based units`(unit: ChronoUnit) {
        invoking {
            DateOffset(1, unit)
        } shouldThrow IllegalArgumentException::class
    }
}
