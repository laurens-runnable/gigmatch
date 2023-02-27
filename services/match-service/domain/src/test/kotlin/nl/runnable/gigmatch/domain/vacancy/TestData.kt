package nl.runnable.gigmatch.domain.vacancy

import java.time.LocalDate
import java.util.*

fun testSkill() = SkillId(UUID.fromString("23134f5f-f865-4a98-995d-9368e1869aa7"))

fun testJob() = Job("Kotlin developer", setOf(testSkill()))

fun testStart(): LocalDate = LocalDate.now().plusMonths(1).withDayOfMonth(1)

fun testVacancyCreated(id: VacancyId) =
    VacancyCreated(id, testJob(), testStart())
