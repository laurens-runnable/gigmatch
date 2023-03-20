package nl.runnable.gigmatch.framework.vacancy

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import nl.runnable.gigmatch.framework.jpa.JpaEntity
import java.util.*

@Entity
@Table(name = "skill")
@EntityListeners(SkillEntityListener::class)
class SkillEntity(id: UUID) : JpaEntity(id) {

    @Column(name = "name", length = 30)
    lateinit var name: String

    @Column(name = "slug", length = 30, unique = true)
    lateinit var slug: String
}
