package nl.runnable.gigmatch.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toInstantAt(offset: TimeOffset): Instant {
    return plusDays(1).atStartOfDay(ZoneId.systemDefault()).plus(offset.amount, offset.unit).toInstant()
}
