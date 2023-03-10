package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Job @JsonCreator constructor(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("experience")
    val experience: Set<Experience>,
) {
    init {
        require(title.isNotBlank()) { "Job title cannot be blank" }
        require(experience.isNotEmpty()) { "Experience cannot be empty" }
    }
}
