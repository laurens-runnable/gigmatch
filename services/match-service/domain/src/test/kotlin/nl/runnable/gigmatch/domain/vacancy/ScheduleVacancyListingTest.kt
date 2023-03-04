package nl.runnable.gigmatch.domain.vacancy

import nl.runnable.gigmatch.domain.whenever
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.time.Instant

class ScheduleVacancyListingTest {

    private lateinit var fixture: AggregateTestFixture<Vacancy>

    private lateinit var eventScheduler: EventScheduler

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Vacancy::class.java)

        eventScheduler = mock {}
        fixture.registerInjectableResource(eventScheduler)
    }

    @Test
    fun `is successful with valid data`() {
        val id = VacancyId.generateRandom()
        fixture
            .given(testVacancyCreated(id))
            .whenever(ScheduleVacancyListing.now(id))
            .expectSuccessfulHandlerExecution()

        verify(eventScheduler).schedule(any<Instant>(), any())
        verifyNoMoreInteractions(eventScheduler)
    }
}
