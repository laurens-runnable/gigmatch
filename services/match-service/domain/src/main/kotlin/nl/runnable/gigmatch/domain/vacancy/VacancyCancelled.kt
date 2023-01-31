package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonProperty

class VacancyCancelled(
    @JsonProperty("id")
    val id: VacancyId
)
