package nl.runnable.gigmatch.framework.jpa

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

/**
 * Abstract base class for JPA Entities that have a programmatically managed primary key.
 */
@MappedSuperclass
abstract class JpaEntity(
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    val id: UUID,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JpaEntity
        return (id != other.id)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
