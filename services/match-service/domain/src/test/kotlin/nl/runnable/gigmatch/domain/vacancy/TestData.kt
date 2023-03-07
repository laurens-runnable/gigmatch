package nl.runnable.gigmatch.domain.vacancy

import java.time.LocalDate
import java.util.*

fun testSkill() = SkillId(UUID.fromString("23134f5f-f865-4a98-995d-9368e1869aa7"))

fun testJob() = Job("Kotlin developer", setOf(testSkill()))

fun testTerm(): Term {
    val start = LocalDate.now().plusMonths(2).withDayOfMonth(1)
    return Term(start, start.plusMonths(6))
}

fun testRate() = Rate(100, Rate.Type.HOURLY)

fun testDeadline(): LocalDate = testTerm().start.minusWeeks(2)

fun testVacancyCreated(id: VacancyId) =
    VacancyOpenedEvent(id, testJob(), testTerm(), testRate(), testDeadline())
