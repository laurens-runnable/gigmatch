package nl.runnable.gigmatch.app.api.v1

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
