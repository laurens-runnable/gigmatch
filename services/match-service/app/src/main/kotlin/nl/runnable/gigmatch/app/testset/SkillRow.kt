package nl.runnable.gigmatch.app.testset

import com.opencsv.bean.CsvBindByName
import java.util.*

class SkillRow {
    @CsvBindByName
    lateinit var id: UUID

    @CsvBindByName
    lateinit var name: String

    @CsvBindByName
    lateinit var slug: String
}
