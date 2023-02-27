package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import java.time.LocalDate

class Vacancy {

    @AggregateIdentifier
    private lateinit var id: VacancyId

    private lateinit var job: Job

    private lateinit var start: LocalDate

    private lateinit var status: VacancyStatus

    @CommandHandler
    private constructor(command: CreateVacancy, skillSpec: SkillSpec) {
        require(command.job.skills.isNotEmpty()) { "Skills cannot be empty" }

        for (skill in command.job.skills) {
            require(skillSpec.skillExists(skill)) { "Skill does not exist $skill" }
        }

        AggregateLifecycle.apply(
            VacancyCreated(
                command.id,
                command.job,
                command.start,
            ),
        )
    }

    @Suppress("unused")
    private constructor()

    @EventHandler
    private fun on(event: VacancyCreated) {
        id = event.id
        job = event.job
        start = event.start
        status = VacancyStatus.OPEN
    }

    @CommandHandler
    private fun handle(command: CloseVacancy) {
        check(status == VacancyStatus.OPEN) { "Vacancy status must be open" }

        AggregateLifecycle.apply(VacancyClosed(id))
    }

    @EventHandler
    private fun on(event: VacancyClosed) {
        status = VacancyStatus.CLOSED
    }

    @CommandHandler
    private fun handle(command: CancelVacancy) {
        check(status == VacancyStatus.OPEN) { "Vacancy status must be open" }

        AggregateLifecycle.apply(VacancyCancelled(id))
    }

    @EventHandler
    private fun on(event: VacancyCancelled) {
        status = VacancyStatus.CANCELLED
    }
}
