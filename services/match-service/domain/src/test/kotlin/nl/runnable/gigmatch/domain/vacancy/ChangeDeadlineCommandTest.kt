package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.whenever
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChangeDeadlineCommandTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    private lateinit var eventScheduler: EventScheduler

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Vacancy::class.java)

        eventScheduler = mock {}
        fixture.registerInjectableResource(eventScheduler)
    }

    @Test
    fun `should be successful when shifted by an offset that results in a date after today`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(ChangeDeadlineCommand.shiftedBy(id, 1, ChronoUnit.DAYS))
            .expectSuccessfulHandlerExecution()

        verify(eventScheduler).schedule(any<Instant>(), any())
        verifyNoMoreInteractions(eventScheduler)
    }

    @Test
    fun `should not be successful when the Vacancy is not open`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .andGiven(VacancyClosedEvent())
            .whenever(ChangeDeadlineCommand.shiftedBy(id, 1, ChronoUnit.DAYS))
            .expectException(IllegalStateException::class.java)
    }

    @Test
    fun `should not be successful when shifted by an offset that results in a date before today`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(ChangeDeadlineCommand.shiftedBy(id, -6, ChronoUnit.MONTHS))
            .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun `should be successful when set to a date after today`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(ChangeDeadlineCommand.toNewDate(id, LocalDate.now().plusMonths(2)))
            .expectSuccessfulHandlerExecution()

        verify(eventScheduler).schedule(any<Instant>(), any())
        verifyNoMoreInteractions(eventScheduler)
    }

    @Test
    fun `should not be successful when changed to a date before today`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(ChangeDeadlineCommand.toNewDate(id, LocalDate.now().minusDays(1)))
            .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun `cannot be instantiated with an offset that is not date-based`() {
        invoking {
            val id = VacancyId.generateRandom()
            ChangeDeadlineCommand.shiftedBy(id, 1, ChronoUnit.MINUTES)
        } shouldThrow java.lang.IllegalArgumentException::class
    }
}
