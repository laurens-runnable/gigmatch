package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class VacancyCreated(
    @JsonProperty("id")
    val id: VacancyId,
    @JsonProperty("job")
    val job: Job,
    @JsonProperty("term")
    val term: Term,
    @JsonProperty("rate")
    val rate: Rate,
    @JsonProperty("deadline")
    val deadline: LocalDate,
)
