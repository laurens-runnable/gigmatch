package nl.runnable.gigmatch.framework.jpa

import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Abstract base class for JPA Entities that have a programmatically managed primary key.
 */
@MappedSuperclass
abstract class JpaEntity(
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    val id: UUID
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
