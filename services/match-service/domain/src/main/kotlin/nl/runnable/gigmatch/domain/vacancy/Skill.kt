package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Skill @JsonCreator constructor(
    @JsonProperty("id")
    val id: SkillId,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("slug")
    val slug: String,
) {
    init {
        require(name.isNotBlank()) {
            "Name cannot be blank"
        }

        require(slug.isNotBlank()) {
            "Slug cannot be blank"
        }
    }
}
