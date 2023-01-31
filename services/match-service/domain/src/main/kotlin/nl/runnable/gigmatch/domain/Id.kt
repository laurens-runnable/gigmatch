package nl.runnable.gigmatch.domain

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

/**
 * Abstract base class for Aggregate identifiers.
 *
 * This implementation is a wrapper around a UUID.
 */
abstract class Id protected constructor(uuid: UUID) {

    @JsonValue
    private val uuid: UUID

    init {
        this.uuid = uuid
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        (other as Id)
        return uuid == other.uuid
    }

    override fun hashCode(): Int = uuid.hashCode()

    override fun toString(): String = uuid.toString()

    fun toUUID() = uuid
}
