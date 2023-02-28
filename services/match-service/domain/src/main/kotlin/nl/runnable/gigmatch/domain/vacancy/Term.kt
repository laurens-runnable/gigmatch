package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Term @JsonCreator constructor(
    @JsonProperty("start")
    val start: LocalDate,
    @JsonProperty("end")
    val end: LocalDate,
) {
    init {
        require(start.isBefore(end)) {
            "Start must be before End"
        }
    }
}
