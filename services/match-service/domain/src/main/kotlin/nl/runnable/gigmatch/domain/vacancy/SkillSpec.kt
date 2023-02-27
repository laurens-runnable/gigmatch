package nl.runnable.gigmatch.domain.vacancy

interface SkillSpec {

    fun skillExists(id: SkillId): Boolean
}
