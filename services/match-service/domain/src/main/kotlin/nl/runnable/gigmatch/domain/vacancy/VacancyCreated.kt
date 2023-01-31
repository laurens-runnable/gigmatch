package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class VacancyCreated(
    @JsonProperty("id")
    val id: VacancyId,
    @JsonProperty("name")
    val jobTitle: String,
    @JsonProperty("start")
    val start: LocalDate,
)
