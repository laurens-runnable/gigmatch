package nl.runnable.gigmatch.domain

import java.time.temporal.TemporalUnit

class DateOffset(val amount: Long, val unit: TemporalUnit) {
    init {
        require(unit.isDateBased) {
            "TemporalUnit must be date-based"
        }
    }
}
