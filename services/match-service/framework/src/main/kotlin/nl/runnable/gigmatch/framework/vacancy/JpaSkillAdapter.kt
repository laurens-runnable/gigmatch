package nl.runnable.gigmatch.framework.vacancy

import nl.runnable.gigmatch.application.vacancy.SkillOutputPort
import nl.runnable.gigmatch.domain.vacancy.Skill
import nl.runnable.gigmatch.domain.vacancy.SkillId
import nl.runnable.gigmatch.domain.vacancy.SkillMustExistSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
internal class JpaSkillAdapter : SkillOutputPort, SkillMustExistSpecification {

    @Autowired
    private lateinit var repository: SkillEntityRepository

    override fun findSkill(id: SkillId): Skill? =
        repository.findByIdOrNull(id.toUUID())?.toSkill()

    override fun saveSkill(skill: Skill) {
        repository.save(skill.toSkillEntity())
    }

    override fun deleteSkill(skillId: SkillId) =
        repository.deleteById(skillId.toUUID())

    override fun isSatisfiedBy(subject: SkillId): Boolean {
        return repository.existsById(subject.toUUID())
    }
}

private fun SkillEntity.toSkill(): Skill {
    return Skill(SkillId(id), name, slug)
}

private fun Skill.toSkillEntity(): SkillEntity {
    val skillEntity = SkillEntity(id.toUUID())
    skillEntity.name = name
    skillEntity.slug = slug
    return skillEntity
}
