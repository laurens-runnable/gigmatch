package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class DeadlineChanged(
    @JsonProperty("newDeadline")
    val newDeadline: LocalDate,
)
