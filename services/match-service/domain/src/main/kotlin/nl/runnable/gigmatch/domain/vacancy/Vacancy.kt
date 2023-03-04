package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.TimeOffset
import nl.runnable.gigmatch.domain.toInstantAt
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
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

    private lateinit var listedAt: Instant

    private var listed = false

    private var listedAtScheduleToken: ScheduleToken? = null

    private lateinit var status: VacancyStatus

    @CommandHandler
    private constructor(command: CreateVacancy, skillMustExist: SkillMustExistSpecification) {
        for (skill in command.job.skills) {
            require(skillMustExist.isSatisfiedBy(skill)) {
                "Skill not found $skill"
            }
        }

        require(command.deadline.isAfter(LocalDate.now())) {
            "Deadline must be after today"
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
                command.listed,
            ),
        )
    }

    @Suppress("unused")
    private constructor()

    @EventSourcingHandler
    private fun on(event: VacancyCreated, eventScheduler: EventScheduler) {
        id = event.id
        job = event.job
        term = event.term
        rate = event.rate
        deadline = event.deadline
        listed = event.listed
        status = VacancyStatus.OPEN

        scheduleDeadlineIfLive(eventScheduler)
    }

    @CommandHandler
    private fun handle(command: CloseVacancy) {
        check(status == VacancyStatus.OPEN) {
            "Vacancy status must be open"
        }

        AggregateLifecycle.apply(VacancyClosed())
    }

    @EventSourcingHandler
    private fun on(event: VacancyClosed) {
        status = VacancyStatus.CLOSED
    }

    @CommandHandler
    private fun handle(command: CancelVacancy) {
        check(status == VacancyStatus.OPEN) { "Vacancy status must be open" }

        AggregateLifecycle.apply(VacancyCancelled())
    }

    @EventSourcingHandler
    private fun on(event: VacancyCancelled) {
        status = VacancyStatus.CANCELLED
    }

    @CommandHandler
    private fun handle(command: ChangeDeadline, scheduler: EventScheduler) {
        check(status == VacancyStatus.OPEN) {
            "Vacancy status must be open"
        }

        val newDeadline = command.resolveNewDate(deadline)
        require(newDeadline.isAfter(LocalDate.now())) {
            "New deadline must be after today"
        }

        AggregateLifecycle.apply(DeadlineChanged(newDeadline))
    }

    @EventSourcingHandler
    private fun on(event: DeadlineChanged, eventScheduler: EventScheduler) {
        deadline = event.newDeadline

        scheduleDeadlineIfLive(eventScheduler)
    }

    @EventSourcingHandler
    private fun on(event: DeadlineExpired) {
        listed = true
        status = VacancyStatus.EXPIRED
    }

    private fun scheduleDeadlineIfLive(eventScheduler: EventScheduler) {
        if (AggregateLifecycle.isLive()) {
            // Schedule at 06:00 in the morning
            val instant = deadline.toInstantAt(TimeOffset(6, ChronoUnit.HOURS))
            deadlineScheduleToken = eventScheduler.schedule(instant, DeadlineExpired())
        }
    }

    @CommandHandler
    private fun handle(command: ScheduleVacancyListing) {
        AggregateLifecycle.apply(VacancyListingScheduled(command.startListingAt))
    }

    @EventSourcingHandler
    private fun on(event: VacancyListingScheduled, eventScheduler: EventScheduler) {
        listedAt = event.newListedAt.toInstant(ZoneOffset.UTC)

        if (AggregateLifecycle.isLive()) {
            listedAtScheduleToken = eventScheduler.schedule(listedAt, VacancyListed())
        }
    }

    @EventSourcingHandler
    private fun on(event: VacancyListed) {
        listed = true
    }
}
