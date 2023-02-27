package nl.runnable.gigmatch.app.testset

import com.opencsv.bean.CsvBindByName
import java.util.*

class VacancyRow {
    @CsvBindByName
    lateinit var id: UUID

    @CsvBindByName
    lateinit var jobTitle: String

    @CsvBindByName
    lateinit var skillId: UUID

    @CsvBindByName
    var monthsFromNow: Int = 0
}
