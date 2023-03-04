package nl.runnable.gigmatch.domain

import java.time.temporal.TemporalUnit

class TimeOffset(val amount: Long, val unit: TemporalUnit) {
    init {
        require(unit.isTimeBased) {
            "TemporalUnit must be time-based"
        }
    }

    val isPositive: Boolean get() = amount >= 0

    val isNegative: Boolean get() = amount < 0
}
