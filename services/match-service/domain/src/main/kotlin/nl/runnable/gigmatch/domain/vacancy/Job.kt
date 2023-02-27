package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Job @JsonCreator constructor(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("skills")
    val skills: Set<SkillId>,
) {
    init {
        require(title.isNotBlank()) { "Job title cannot be blank" }
        require(skills.isNotEmpty()) { "Skills cannot be empty" }
    }
}
