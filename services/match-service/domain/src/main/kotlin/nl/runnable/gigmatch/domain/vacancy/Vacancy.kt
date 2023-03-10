package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.TimeOffset
import nl.runnable.gigmatch.domain.toInstantAt
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import java.time.LocalDate
import java.time.temporal.ChronoUnit

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

    private var deadlineScheduleToken: ScheduleToken? = null

    private lateinit var status: Status

    @CommandHandler
    private constructor(command: OpenVacancyCommand, skillMustExist: SkillMustExistSpecification) {
        for (skillId in command.job.experience.map { it.skillId }) {
            require(skillMustExist.isSatisfiedBy(skillId)) {
                "Skill not found $skillId"
            }
        }

        require(command.deadline.isAfter(LocalDate.now())) {
            "Deadline must be after today"
        }
        require(command.deadline.isBefore(command.term.start)) {
            "Deadline must be before start of term"
        }

        AggregateLifecycle.apply(
            VacancyOpenedEvent(
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

    @EventSourcingHandler
    private fun on(event: VacancyOpenedEvent, eventScheduler: EventScheduler) {
        id = event.id
        job = event.job
        term = event.term
        rate = event.rate
        deadline = event.deadline
        status = Status.OPEN

        scheduleDeadlineIfLive(eventScheduler)
    }

    @CommandHandler
    private fun handle(command: CloseVacancyCommand) {
        check(status == Status.OPEN) {
            "Vacancy status must be open"
        }

        AggregateLifecycle.apply(VacancyClosedEvent())
    }

    @EventSourcingHandler
    private fun on(event: VacancyClosedEvent) {
        status = Status.CLOSED
    }

    @CommandHandler
    private fun handle(command: CancelVacancyCommand) {
        check(status == Status.OPEN) { "Vacancy status must be open" }

        AggregateLifecycle.apply(VacancyCancelledEvent())
    }

    @EventSourcingHandler
    private fun on(event: VacancyCancelledEvent) {
        status = Status.CANCELLED
    }

    @CommandHandler
    private fun handle(command: ChangeDeadlineCommand, scheduler: EventScheduler) {
        check(status == Status.OPEN) {
            "Vacancy status must be open"
        }

        val newDeadline = command.resolveNewDate(deadline)
        require(newDeadline.isAfter(LocalDate.now())) {
            "New deadline must be after today"
        }

        AggregateLifecycle.apply(DeadlineChangedEvent(newDeadline))
    }

    @EventSourcingHandler
    private fun on(event: DeadlineChangedEvent, eventScheduler: EventScheduler) {
        deadline = event.newDeadline

        scheduleDeadlineIfLive(eventScheduler)
    }

    @EventSourcingHandler
    private fun on(event: DeadlineExpiredEvent) {
        status = Status.EXPIRED
    }

    private fun scheduleDeadlineIfLive(eventScheduler: EventScheduler) {
        if (AggregateLifecycle.isLive()) {
            // Schedule at 06:00 in the morning
            val instant = deadline.toInstantAt(TimeOffset(6, ChronoUnit.HOURS))
            deadlineScheduleToken = eventScheduler.schedule(instant, DeadlineExpiredEvent())
        }
    }
}
