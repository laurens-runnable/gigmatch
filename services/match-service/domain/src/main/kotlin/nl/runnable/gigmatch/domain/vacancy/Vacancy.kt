package nl.runnable.gigmatch.domain.vacancy

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import java.time.LocalDate

/**
 * The root aggregate for the Vacancy process.
 */
class Vacancy {

    @AggregateIdentifier
    private lateinit var id: VacancyId

    private lateinit var job: Job

    private lateinit var term: Term

    private lateinit var rate: Rate

    private lateinit var deadline: LocalDate

    private lateinit var status: VacancyStatus

    @CommandHandler
    private constructor(command: CreateVacancy, skillMustExist: SkillMustExistSpecification) {
        for (skill in command.job.skills) {
            require(skillMustExist.isSatisfiedBy(skill)) {
                "Skill not found $skill"
            }
        }

        require(command.deadline.isBefore(command.term.start)) {
            "Deadline must be before start of term"
        }

        AggregateLifecycle.apply(
            VacancyCreated(
                command.id,
                command.job,
                command.term,
                command.rate,
                command.deadline,
            ),
        )
    }

    @Suppress("unused")
    private constructor()

    @EventHandler
    private fun on(event: VacancyCreated) {
        id = event.id
        job = event.job
        term = event.term
        rate = event.rate
        deadline = event.deadline
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
