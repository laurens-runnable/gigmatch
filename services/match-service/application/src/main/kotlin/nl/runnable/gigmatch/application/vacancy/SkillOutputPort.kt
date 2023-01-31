package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.Skill
import nl.runnable.gigmatch.domain.vacancy.SkillId

interface SkillOutputPort {

    fun findGenre(id: SkillId): Skill?

    fun saveGenre(skill: Skill)

    fun deleteGenre(skillId: SkillId)

}
