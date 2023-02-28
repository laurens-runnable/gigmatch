package nl.runnable.gigmatch.app.testset

import com.opencsv.bean.CsvBindByName
import nl.runnable.gigmatch.commands.RateType
import java.util.*

class VacancyRow {
    @CsvBindByName
    lateinit var id: UUID

    @CsvBindByName
    lateinit var jobTitle: String

    @CsvBindByName
    lateinit var skillId: UUID

    @CsvBindByName
    var startMonthsFromNow: Long = 0

    @CsvBindByName
    var durationMonths: Long = 0

    @CsvBindByName
    var rateAmount: Int = 0

    @CsvBindByName
    var rateType: RateType = RateType.HOURLY

    @CsvBindByName
    var deadlineWeeksBefore: Long = 0
}
