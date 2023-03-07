package nl.runnable.gigmatch.application.vacancy

import nl.runnable.gigmatch.domain.vacancy.OpenVacancyCommand

interface VacancyOutputPort {

    fun send(command: OpenVacancyCommand)
}
