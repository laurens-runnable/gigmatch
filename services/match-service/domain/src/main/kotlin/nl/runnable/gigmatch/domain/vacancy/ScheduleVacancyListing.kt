package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.TimeOffset
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScheduleVacancyListing(
    @TargetAggregateIdentifier
    private val id: VacancyId,
    private val offset: TimeOffset,
) {
    init {
        require(offset.isPositive) {
            "Time offset must be positive"
        }
    }

    val startListingAt: LocalDateTime get() = LocalDateTime.now().plus(offset.amount, offset.unit)

    companion object {
        fun now(id: VacancyId) = ScheduleVacancyListing(id, TimeOffset(0, ChronoUnit.SECONDS))

        fun after(id: VacancyId, amount: Int, unit: ChronoUnit) =
            ScheduleVacancyListing(id, TimeOffset(amount.toLong(), unit))
    }
}
