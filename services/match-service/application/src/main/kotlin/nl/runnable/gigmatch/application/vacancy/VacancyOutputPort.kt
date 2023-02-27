package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.CreateVacancy

interface VacancyOutputPort {

    fun send(command: CreateVacancy)
}
