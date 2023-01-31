package nl.runnable.gigmatch.framework.genre

import nl.runnable.gigmatch.application.vacancy.SkillOutputPort
import nl.runnable.gigmatch.domain.vacancy.Skill
import nl.runnable.gigmatch.domain.vacancy.SkillId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
internal class JpaSkillAdapter : SkillOutputPort {

    @Autowired
    private lateinit var repository: GenreEntityRepository

    override fun findGenre(id: SkillId): Skill? =
        repository.findByIdOrNull(id.toUUID())?.toGenre()

    override fun saveGenre(skill: Skill) {
        repository.save(skill.toGenreEntity())
    }

    override fun deleteGenre(skillId: SkillId) =
        repository.deleteById(skillId.toUUID())
}


private fun SkillEntity.toGenre(): Skill {
    return Skill(SkillId(id), name, slug)
}

private fun Skill.toGenreEntity(): SkillEntity {
    val skillEntity = SkillEntity(id.toUUID())
    skillEntity.name = name
    skillEntity.slug = slug
    return skillEntity
}
