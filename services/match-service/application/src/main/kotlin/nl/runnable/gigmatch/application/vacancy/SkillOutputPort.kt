package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.Skill
import nl.runnable.gigmatch.domain.vacancy.SkillId

interface SkillOutputPort {

    fun findSkill(id: SkillId): Skill?

    fun saveSkill(skill: Skill)

    fun deleteSkill(skillId: SkillId)
}
