package nl.runnable.gigmatch.framework.genre

import nl.runnable.gigmatch.framework.jpa.JpaEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Entity
@Table(name = "genre")
@EntityListeners(SkillEntityListener::class)
class SkillEntity(id: UUID) : JpaEntity(id) {

    @Column(name = "name", length = 30)
    lateinit var name: String

    @Column(name = "slug", length = 30, unique = true)
    lateinit var slug: String

}
