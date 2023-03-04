package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class VacancyListingScheduled(
    @JsonProperty("newListedAt")
    val newListedAt: LocalDateTime,
)
