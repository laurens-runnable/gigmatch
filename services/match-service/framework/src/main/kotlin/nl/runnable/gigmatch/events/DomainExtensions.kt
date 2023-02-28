package nl.runnable.gigmatch.events

import nl.runnable.gigmatch.domain.vacancy.Rate

fun Rate.Type.toFrameworkCounterpart() = when (this) {
    Rate.Type.HOURLY -> RateType.HOURLY
    Rate.Type.DAILY -> RateType.DAILY
    Rate.Type.FIXED -> RateType.FIXED
}
