package nl.runnable.gigmatch.domain.vacancy

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the rate.
 *
 * To keep the domain model simple, the currency has been left out.
 */
data class Rate @JsonCreator constructor(
    @JsonProperty("amount")
    val amount: Int,

    @JsonProperty("type")
    val type: Type,
) {
    enum class Type {
        HOURLY, DAILY, FIXED
    }

    init {
        require(amount >= 0) {
            "Amount cannot be less than 0"
        }
    }
}
