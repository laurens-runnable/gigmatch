package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.DateOffset
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChangeDeadline private constructor(
    @TargetAggregateIdentifier
    private val id: VacancyId,
    private val offset: DateOffset?,
    private val date: LocalDate?,
) {

    fun resolveNewDate(currentDeadline: LocalDate): LocalDate {
        return if (offset != null) {
            currentDeadline.plus(offset.amount, offset.unit)
        } else {
            require(date != null)
            date
        }
    }

    companion object {
        fun shiftedBy(id: VacancyId, amount: Int, unit: ChronoUnit) =
            ChangeDeadline(id, DateOffset(amount.toLong(), unit), null)

        fun toNewDate(id: VacancyId, date: LocalDate) = ChangeDeadline(id, null, date)
    }
}
