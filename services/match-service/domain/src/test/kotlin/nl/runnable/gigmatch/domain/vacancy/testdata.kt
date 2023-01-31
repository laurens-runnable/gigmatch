package nl.runnable.gigmatch.domain.vacancy

import java.time.LocalDate

fun testJobTitle() = "Kotlin developer"

fun testStart(): LocalDate = LocalDate.now().plusMonths(1).withDayOfMonth(1)

fun testVacancyCreated(id: VacancyId) =
    VacancyCreated(id, testJobTitle(), testStart())
